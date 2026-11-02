#!/usr/bin/env python3
"""
Assign cross-team PR buddies from roster.json.

Rules:
  * Only active roster members take part; team sizes are counted after
    inactive members are removed.
  * Every person gets a primary buddy from a different team.
  * People with no active teammates (a team of one) also get a second
    buddy, again from a different team than their own, and different from
    their primary.
  * All primaries are assigned before any seconds.
  * Buddy load is spread out: the person picked the fewest times is
    preferred, and the most even of 500 random attempts is kept.
    Reciprocal pairs (A to be reviewed by B, and B to be reviewed by A)
    are avoided when possible.
  * Output is reproducible: the seed defaults to the month (2026-11 -> 202611).

Usage:
    pr_buddies.py                              # current UTC month, prints only
    pr_buddies.py --month 2026-12 --out assignments.json
    pr_buddies.py --seed 42                    # override the month-derived seed

Standard library only; runs on Python 3.9+.
"""
import argparse
import datetime
import json
import os
import random
import re
import sys
from collections import defaultdict

HERE = os.path.dirname(os.path.realpath(__file__))
DEFAULT_ROSTER = os.path.join(HERE, "roster.json")

MEMBER_KEYS = {"github", "team", "active", "note"}
ROSTER_KEYS = {"members"}
# GitHub logins: alphanumerics and single hyphens, up to 39 characters.
HANDLE_RE = re.compile(r"^[A-Za-z0-9](?:[A-Za-z0-9-]{0,38})$")
MONTH_RE = re.compile(r"^\d{4}-(0[1-9]|1[0-2])$")


class BuddyError(Exception):
    """A problem with the input; the message is shown to the user as-is."""


def load_roster(path):
    """Read and strictly validate roster.json. Returns the list of members.

    Each member is a dict with "github", "team", "active" (defaulted to True)
    and optionally "note".
    """
    if not os.path.isfile(path):
        raise BuddyError(f"Roster file not found: {path}")
    with open(path, encoding="utf-8") as f:
        text = f.read()
    try:
        data = json.loads(text)
    except json.JSONDecodeError as e:
        raise BuddyError(f"{path}:{e.lineno}:{e.colno}: invalid JSON: {e.msg}")
    return validate_roster(data, path)


def validate_roster(data, path="roster.json"):
    if not isinstance(data, dict):
        raise BuddyError(f"{path}: top level must be an object with a 'members' list")
    unknown = sorted(set(data) - ROSTER_KEYS)
    if unknown:
        raise BuddyError(f"{path}: unknown top-level key(s): {', '.join(unknown)}")
    members = data.get("members")
    if not isinstance(members, list):
        raise BuddyError(f"{path}: 'members' must be a list")

    seen = {}
    result = []
    for i, m in enumerate(members):
        where = f"{path}: members[{i}]"
        if not isinstance(m, dict):
            raise BuddyError(f"{where}: must be an object")
        if isinstance(m.get("github"), str):
            where = f"{where} ({m['github']})"
        unknown = sorted(set(m) - MEMBER_KEYS)
        if unknown:
            raise BuddyError(
                f"{where}: unknown key(s): {', '.join(unknown)} "
                f"(allowed: {', '.join(sorted(MEMBER_KEYS))})"
            )
        for key in ("github", "team"):
            if key not in m:
                raise BuddyError(f"{where}: missing '{key}'")
            if not isinstance(m[key], str) or not m[key].strip():
                raise BuddyError(f"{where}: '{key}' must be a non-empty string")
        handle = m["github"].strip()
        if not HANDLE_RE.match(handle):
            raise BuddyError(
                f"{where}: '{handle}' is not a valid GitHub handle (no '@')"
            )
        active = m.get("active", True)
        if not isinstance(active, bool):
            raise BuddyError(f"{where}: 'active' must be true or false")
        if "note" in m and not isinstance(m["note"], str):
            raise BuddyError(f"{where}: 'note' must be a string")
        key = handle.lower()
        if key in seen:
            raise BuddyError(
                f"'{handle}' appears more than once in {path} "
                f"(members[{seen[key]}] and members[{i}])"
            )
        seen[key] = i
        member = {"github": handle, "team": m["team"].strip(), "active": active}
        if "note" in m:
            member["note"] = m["note"]
        result.append(member)
    return result


def active_teams(members):
    """team -> [handles] for active members, in roster order."""
    teams = {}
    for m in members:
        if m["active"]:
            teams.setdefault(m["team"], []).append(m["github"])
    return teams


def pick(person, candidates, load, assigned, rng):
    """Least-loaded candidate, avoiding reciprocal pairs when possible."""
    non_reciprocal = [c for c in candidates if person not in assigned[c]]
    pool = non_reciprocal or candidates
    lowest = min(load[c] for c in pool)
    return rng.choice([c for c in pool if load[c] == lowest])


def assign(teams, rng):
    team_of = {}
    for team, members in teams.items():
        for m in members:
            if m in team_of:
                raise BuddyError(f"'{m}' appears in more than one team")
            team_of[m] = team

    if len(teams) < 2:
        raise BuddyError("Need at least two teams to make cross-team buddies.")

    people = list(team_of)
    load = defaultdict(int)      # times each person has been picked as a buddy
    assigned = defaultdict(list)  # person -> [buddies]

    # Round 1: everyone gets a primary.
    order = people[:]
    rng.shuffle(order)
    for p in order:
        candidates = [c for c in people if team_of[c] != team_of[p]]
        b = pick(p, candidates, load, assigned, rng)
        assigned[p].append(b)
        load[b] += 1

    # Round 2: solo-team people get a suggested second.
    solos = [p for p in order if len(teams[team_of[p]]) == 1]
    for p in solos:
        primary = assigned[p][0]
        candidates = [
            c for c in people if team_of[c] != team_of[p] and c != primary
        ]
        if not candidates:
            continue
        # Prefer a second from a different team than the primary, for variety.
        varied = [c for c in candidates if team_of[c] != team_of[primary]]
        b = pick(p, varied or candidates, load, assigned, rng)
        assigned[p].append(b)
        load[b] += 1

    return people, assigned


def score(assigned):
    """Lower is better: spread of buddy load first, then reciprocal pairs."""
    load = defaultdict(int)
    for buddies in assigned.values():
        for b in buddies:
            load[b] += 1
    people = list(assigned)
    loads = [load[p] for p in people]
    reciprocal = sum(
        1 for p in people for b in assigned[p] if p in assigned.get(b, [])
    )
    return (max(loads) - min(loads), max(loads), reciprocal)


def best_assignment(teams, rng, attempts=500):
    best = None
    for _ in range(attempts):
        people, assigned = assign(teams, rng)
        s = score(assigned)
        if best is None or s < best[0]:
            best = (s, people, assigned)
    return best[1], best[2]


def seed_for_month(month):
    """'2026-11' -> 202611."""
    return int(month.replace("-", ""))


def current_month(now=None):
    now = now or datetime.datetime.now(datetime.timezone.utc)
    return now.strftime("%Y-%m")


def validate_month(month):
    if not MONTH_RE.match(month):
        raise BuddyError(f"--month must look like YYYY-MM, got '{month}'")
    return month


def generate(members, month, seed=None):
    """Build the assignments document for `month`."""
    if seed is None:
        seed = seed_for_month(month)
    people, assigned = best_assignment(active_teams(members), random.Random(seed))
    buddies = {p: list(assigned[p]) for p in sorted(people, key=str.lower)}
    return {"month": month, "seed": seed, "buddies": buddies}


def readable_lines(doc):
    return [
        f"{p} is reviewed by {', '.join(b)}" for p, b in doc["buddies"].items()
    ]


def markdown_table(doc, members):
    team_of = {m["github"]: m["team"] for m in members}

    def cell(handle):
        return f"{handle} ({team_of.get(handle, '?')})"

    rows = [
        "| Author | Primary buddy | Second buddy |",
        "| --- | --- | --- |",
    ]
    for p, b in doc["buddies"].items():
        second = cell(b[1]) if len(b) > 1 else ""
        rows.append(f"| {cell(p)} | {cell(b[0])} | {second} |")
    return "\n".join(rows)


def write_json(path, doc):
    with open(path, "w", encoding="utf-8") as f:
        json.dump(doc, f, indent=2)
        f.write("\n")


def main(argv=None):
    ap = argparse.ArgumentParser(description="Assign cross-team PR buddies.")
    ap.add_argument(
        "--roster",
        default=DEFAULT_ROSTER,
        help="roster file (default: roster.json next to this script)",
    )
    ap.add_argument(
        "--month", help="month to generate, YYYY-MM (default: current UTC month)"
    )
    ap.add_argument(
        "--seed", type=int, help="random seed (default: derived from --month)"
    )
    ap.add_argument("--out", help="write assignments JSON here")
    ap.add_argument("--table-out", help="write the Markdown pairings table here")
    args = ap.parse_args(argv)

    try:
        month = validate_month(args.month or current_month())
        members = load_roster(args.roster)
        doc = generate(members, month, args.seed)
    except BuddyError as e:
        sys.exit(str(e))

    table = markdown_table(doc, members)
    print(f"PR buddies for {doc['month']} (seed {doc['seed']})\n")
    print("\n".join(readable_lines(doc)))
    print()
    print(table)
    if args.out:
        write_json(args.out, doc)
    if args.table_out:
        with open(args.table_out, "w", encoding="utf-8") as f:
            f.write(table + "\n")


if __name__ == "__main__":
    main()
