#!/usr/bin/env python3
"""
Print the GitHub handles to assign to a PR, one per line.

Assignees are the author's buddies from assignments.json (primary, then
second) followed by the author's active teammates from roster.json, minus the
author, de-duplicated and capped at GitHub's 10-assignee limit (buddies first).

Prints nothing (and exits 0) when the PR should be skipped: bots, automated
PRs, and authors who aren't in the roster. Log lines go to stderr.

Usage:
    assignees.py <author> [--title "PR title"]

Standard library only; runs on Python 3.9+.
"""
import argparse
import json
import os
import sys

from pr_buddies import DEFAULT_ROSTER, HERE, BuddyError, current_month, load_roster

DEFAULT_ASSIGNMENTS = os.path.join(HERE, "assignments.json")
MAX_ASSIGNEES = 10
SKIPPED_AUTHORS = {"renovate", "dependabot", "github-actions"}
SKIPPED_TITLE_PREFIXES = ("[automated]",)


def log(level, message):
    if os.environ.get("GITHUB_ACTIONS") == "true" and level in ("warning", "error"):
        print(f"::{level}::{message}", file=sys.stderr)
    else:
        print(f"{level}: {message}", file=sys.stderr)


def skip_reason(author, title=None):
    """Why this PR shouldn't get assignees, or None."""
    login = author.lower()
    if login.endswith("[bot]"):
        return f"author {author} is a bot"
    if login in SKIPPED_AUTHORS:
        return f"author {author} is an automation account"
    if title and title.strip().lower().startswith(SKIPPED_TITLE_PREFIXES):
        return f"title starts with {SKIPPED_TITLE_PREFIXES[0]}"
    return None


def load_assignments(path):
    if not os.path.isfile(path):
        raise BuddyError(f"Assignments file not found: {path}")
    with open(path, encoding="utf-8") as f:
        try:
            doc = json.load(f)
        except json.JSONDecodeError as e:
            raise BuddyError(f"{path}:{e.lineno}:{e.colno}: invalid JSON: {e.msg}")
    if not isinstance(doc, dict) or not isinstance(doc.get("buddies"), dict):
        raise BuddyError(f"{path}: expected an object with a 'buddies' map")
    return doc


def compute_assignees(author, members, buddies, warn=lambda msg: None):
    """Assignee handles for `author`, or None if the author isn't in the roster.

    Handles are matched case-insensitively and returned as spelled in the roster.
    """
    by_login = {m["github"].lower(): m for m in members}
    me = by_login.get(author.lower())
    if me is None:
        return None
    if not me["active"]:
        warn(f"{me['github']} is inactive in the roster; assigning teammates only")

    buddies_ci = {k.lower(): v for k, v in buddies.items()}
    candidates = []
    for b in buddies_ci.get(me["github"].lower(), []):
        member = by_login.get(b.lower())
        if member is None or not member["active"]:
            warn(f"buddy {b} is no longer active in the roster; skipping")
            continue
        candidates.append(member["github"])
    if me["active"] and not candidates:
        warn(f"{me['github']} has no buddies in assignments.json")
    candidates += [
        m["github"] for m in members if m["active"] and m["team"] == me["team"]
    ]

    result = []
    seen = {me["github"].lower()}
    for c in candidates:
        if c.lower() not in seen:
            seen.add(c.lower())
            result.append(c)
    if len(result) > MAX_ASSIGNEES:
        warn(
            f"{len(result)} assignees for {me['github']}; "
            f"keeping the first {MAX_ASSIGNEES} (GitHub's limit)"
        )
        result = result[:MAX_ASSIGNEES]
    return result


def main(argv=None, month=None):
    ap = argparse.ArgumentParser(description="Print PR assignees for an author.")
    ap.add_argument("author", help="PR author's GitHub login")
    ap.add_argument("--title", help="PR title, used to skip automated PRs")
    ap.add_argument("--roster", default=DEFAULT_ROSTER)
    ap.add_argument("--assignments", default=DEFAULT_ASSIGNMENTS)
    args = ap.parse_args(argv)

    reason = skip_reason(args.author, args.title)
    if reason:
        log("info", f"Skipping: {reason}")
        return 0

    try:
        members = load_roster(args.roster)
        doc = load_assignments(args.assignments)
    except BuddyError as e:
        log("error", str(e))
        return 1

    expected = month or current_month()
    if doc.get("month") != expected:
        log(
            "warning",
            f"assignments.json is for {doc.get('month')}, not {expected} "
            "(has this month's rotation PR merged?); using it anyway",
        )

    result = compute_assignees(
        args.author, members, doc["buddies"], warn=lambda m: log("warning", m)
    )
    if result is None:
        log("info", f"Skipping: {args.author} is not in the roster")
        return 0
    if not result:
        log("info", f"No assignees for {args.author}")
    for handle in result:
        print(handle)
    return 0


if __name__ == "__main__":
    sys.exit(main())
