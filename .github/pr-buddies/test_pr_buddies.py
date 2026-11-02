import contextlib
import io
import json
import os
import random
import tempfile
import unittest

from pr_buddies import (
    BuddyError,
    active_teams,
    best_assignment,
    generate,
    load_roster,
    main,
    markdown_table,
    score,
    seed_for_month,
    validate_roster,
)


def roster(*members):
    """roster("a:t1", "b:t2", "c:t2:inactive") -> validated member list."""
    out = []
    for spec in members:
        parts = spec.split(":")
        m = {"github": parts[0], "team": parts[1]}
        if len(parts) > 2 and parts[2] == "inactive":
            m["active"] = False
        out.append(m)
    return validate_roster({"members": out})


def team_lookup(members):
    return {m["github"]: m["team"] for m in members}


def has_active_teammate(members, handle):
    team = team_lookup(members)[handle]
    return any(
        m["active"] and m["team"] == team and m["github"] != handle for m in members
    )


MIXED = roster(
    "a1:alpha", "a2:alpha", "a3:alpha",
    "b1:beta", "b2:beta",
    "c1:gamma",
    "d1:delta",
    "e1:epsilon", "e2:epsilon", "e3:epsilon:inactive",
)


class AssignmentRulesTest(unittest.TestCase):
    def check_rules(self, members, doc):
        team_of = team_lookup(members)
        active = {m["github"] for m in members if m["active"]}
        self.assertEqual(active, set(doc["buddies"]))
        for person, buddies in doc["buddies"].items():
            self.assertNotIn(person, buddies)
            self.assertNotEqual(team_of[person], team_of[buddies[0]])
            for b in buddies:
                self.assertIn(b, active)
            if has_active_teammate(members, person):
                self.assertEqual(1, len(buddies), person)
            else:
                self.assertEqual(2, len(buddies), person)
                self.assertNotEqual(buddies[0], buddies[1])
                self.assertNotEqual(team_of[person], team_of[buddies[1]])

    def test_rules_hold_across_seeds(self):
        for seed in range(25):
            self.check_rules(MIXED, generate(MIXED, "2026-11", seed))

    def test_inactive_members_never_assigned(self):
        doc = generate(MIXED, "2026-11")
        self.assertNotIn("e3", doc["buddies"])
        for buddies in doc["buddies"].values():
            self.assertNotIn("e3", buddies)

    def test_second_prefers_team_other_than_primary(self):
        team_of = team_lookup(MIXED)
        for seed in range(25):
            doc = generate(MIXED, "2026-11", seed)
            for person in ("c1", "d1"):
                primary, second = doc["buddies"][person]
                self.assertNotEqual(team_of[primary], team_of[second])

    def test_inactive_teammate_makes_a_solo(self):
        both = roster("x1:xray", "x2:xray", "y1:yankee", "y2:yankee", "z1:zulu", "z2:zulu")
        self.assertEqual(1, len(generate(both, "2026-11")["buddies"]["x1"]))

        x2_away = roster(
            "x1:xray", "x2:xray:inactive", "y1:yankee", "y2:yankee", "z1:zulu", "z2:zulu"
        )
        doc = generate(x2_away, "2026-11")
        self.assertEqual(2, len(doc["buddies"]["x1"]))
        self.check_rules(x2_away, doc)

    def test_no_reciprocal_pairs_when_avoidable(self):
        members = roster("a1:alpha", "a2:alpha", "b1:beta", "b2:beta", "c1:gamma", "c2:gamma")
        for seed in range(10):
            doc = generate(members, "2026-11", seed)
            for person, buddies in doc["buddies"].items():
                for b in buddies:
                    self.assertNotIn(person, doc["buddies"][b], f"seed {seed}: {person}<->{b}")

    def test_load_is_balanced(self):
        people, assigned = best_assignment(active_teams(MIXED), random.Random(1))
        spread, _, _ = score(assigned)
        self.assertLessEqual(spread, 1)

    def test_same_seed_same_output(self):
        self.assertEqual(generate(MIXED, "2026-11", 7), generate(MIXED, "2026-11", 7))

    def test_seed_derived_from_month(self):
        self.assertEqual(202611, seed_for_month("2026-11"))
        doc = generate(MIXED, "2026-11")
        self.assertEqual(202611, doc["seed"])
        self.assertEqual(doc, generate(MIXED, "2026-11", 202611))

    def test_fewer_than_two_teams(self):
        with self.assertRaisesRegex(BuddyError, "at least two teams"):
            generate(roster("a:alpha", "b:alpha"), "2026-11")

    def test_inactive_members_count_against_two_teams(self):
        with self.assertRaisesRegex(BuddyError, "at least two teams"):
            generate(roster("a:alpha", "b:alpha", "c:beta:inactive"), "2026-11")

    def test_markdown_table(self):
        members = roster("a:alpha", "b:alpha", "c:beta")
        table = markdown_table({"buddies": {"a": ["c"], "c": ["a", "b"]}}, members)
        self.assertIn("| a (alpha) | c (beta) |  |", table)
        self.assertIn("| c (beta) | a (alpha) | b (alpha) |", table)


class RosterValidationTest(unittest.TestCase):
    def write(self, text):
        fd, path = tempfile.mkstemp(suffix=".json")
        with os.fdopen(fd, "w") as f:
            f.write(text)
        self.addCleanup(os.remove, path)
        return path

    def load(self, data):
        return load_roster(self.write(json.dumps(data)))

    def test_active_defaults_true_and_note_kept(self):
        members = self.load({"members": [
            {"github": "a", "team": "t"},
            {"github": "b", "team": "u", "active": False, "note": "leave"},
        ]})
        self.assertTrue(members[0]["active"])
        self.assertFalse(members[1]["active"])
        self.assertEqual("leave", members[1]["note"])

    def test_invalid_json_reports_line_and_column(self):
        path = self.write('{\n  "members": [\n    { "github": "a", }\n  ]\n}\n')
        with self.assertRaisesRegex(BuddyError, r":3:\d+: invalid JSON"):
            load_roster(path)

    def test_missing_github(self):
        with self.assertRaisesRegex(BuddyError, r"members\[0\]: missing 'github'"):
            self.load({"members": [{"team": "t"}]})

    def test_missing_team(self):
        with self.assertRaisesRegex(BuddyError, r"members\[0\] \(a\): missing 'team'"):
            self.load({"members": [{"github": "a"}]})

    def test_duplicate_handle_case_insensitive(self):
        with self.assertRaisesRegex(BuddyError, "'Alice' appears more than once"):
            self.load({"members": [
                {"github": "alice", "team": "t"},
                {"github": "Alice", "team": "u", "active": False},
            ]})

    def test_unknown_member_key(self):
        with self.assertRaisesRegex(BuddyError, "unknown key.*: actve"):
            self.load({"members": [{"github": "a", "team": "t", "actve": False}]})

    def test_unknown_top_level_key(self):
        with self.assertRaisesRegex(BuddyError, "unknown top-level key.*: teams"):
            self.load({"members": [], "teams": []})

    def test_active_must_be_boolean(self):
        with self.assertRaisesRegex(BuddyError, "'active' must be true or false"):
            self.load({"members": [{"github": "a", "team": "t", "active": "no"}]})

    def test_handle_with_at_sign_rejected(self):
        with self.assertRaisesRegex(BuddyError, "not a valid GitHub handle"):
            self.load({"members": [{"github": "@a", "team": "t"}]})

    def test_missing_file(self):
        with self.assertRaisesRegex(BuddyError, "Roster file not found"):
            load_roster("/nonexistent/roster.json")


class CliTest(unittest.TestCase):
    def test_writes_assignments_and_table(self):
        with tempfile.TemporaryDirectory() as d:
            roster_path = os.path.join(d, "roster.json")
            with open(roster_path, "w") as f:
                json.dump({"members": [
                    {"github": "a", "team": "t"},
                    {"github": "b", "team": "t"},
                    {"github": "c", "team": "u"},
                ]}, f)
            out, table = os.path.join(d, "out.json"), os.path.join(d, "table.md")
            stdout = io.StringIO()
            with contextlib.redirect_stdout(stdout):
                main(["--roster", roster_path, "--month", "2026-12",
                      "--out", out, "--table-out", table])
            with open(out) as f:
                doc = json.load(f)
            self.assertEqual("2026-12", doc["month"])
            self.assertEqual(202612, doc["seed"])
            self.assertEqual(["a", "b", "c"], list(doc["buddies"]))
            self.assertIn("c is reviewed by", stdout.getvalue())
            with open(table) as f:
                self.assertTrue(f.read().startswith("| Author |"))

    def test_errors_exit_with_message(self):
        stderr = io.StringIO()
        with contextlib.redirect_stderr(stderr), self.assertRaises(SystemExit) as cm:
            main(["--roster", "/nonexistent.json", "--month", "2026-12"])
        self.assertEqual("Roster file not found: /nonexistent.json", cm.exception.code)

    def test_bad_month(self):
        with self.assertRaises(SystemExit) as cm:
            main(["--month", "2026-13"])
        self.assertIn("YYYY-MM", cm.exception.code)


if __name__ == "__main__":
    unittest.main()
