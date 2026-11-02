import contextlib
import io
import json
import os
import tempfile
import unittest

from assignees import MAX_ASSIGNEES, compute_assignees, main, skip_reason
from pr_buddies import validate_roster


def members(*specs):
    out = []
    for spec in specs:
        parts = spec.split(":")
        m = {"github": parts[0], "team": parts[1]}
        if len(parts) > 2 and parts[2] == "inactive":
            m["active"] = False
        out.append(m)
    return validate_roster({"members": out})


ROSTER = members(
    "alice:coaching", "carol:coaching", "erin:coaching:inactive",
    "bob:clubs",
    "frank:sports", "grace:sports",
)
BUDDIES = {
    "alice": ["bob"],
    "carol": ["frank"],
    "bob": ["carol", "grace"],
    "frank": ["alice"],
    "grace": ["bob"],
}


class ComputeAssigneesTest(unittest.TestCase):
    def test_buddies_then_active_teammates(self):
        self.assertEqual(["bob", "carol"], compute_assignees("alice", ROSTER, BUDDIES))

    def test_author_not_in_roster(self):
        self.assertIsNone(compute_assignees("mallory", ROSTER, BUDDIES))

    def test_author_with_no_teammates_gets_both_buddies(self):
        self.assertEqual(["carol", "grace"], compute_assignees("bob", ROSTER, BUDDIES))

    def test_case_insensitive_author(self):
        self.assertEqual(["bob", "carol"], compute_assignees("ALICE", ROSTER, BUDDIES))

    def test_no_self_assignment_and_no_duplicates(self):
        roster = members("a:t", "b:t", "c:u")
        # b is both a buddy (bad data) and a teammate; a lists itself.
        result = compute_assignees("a", roster, {"a": ["b", "a", "c"]})
        self.assertEqual(["b", "c"], result)

    def test_cap_at_ten_buddies_first(self):
        team = [f"t{i}:big" for i in range(15)]
        roster = members(*team, "x1:other", "x2:third")
        warnings = []
        result = compute_assignees(
            "t0", roster, {"t0": ["x1", "x2"]}, warn=warnings.append
        )
        self.assertEqual(MAX_ASSIGNEES, len(result))
        self.assertEqual(["x1", "x2"], result[:2])
        self.assertEqual([f"t{i}" for i in range(1, 9)], result[2:])
        self.assertTrue(any("keeping the first 10" in w for w in warnings))

    def test_inactive_buddy_dropped(self):
        roster = members("a:t", "b:u", "c:v:inactive")
        warnings = []
        self.assertEqual(["b"], compute_assignees("a", roster, {"a": ["c", "b"]}, warnings.append))
        self.assertTrue(any("c is no longer active" in w for w in warnings))

    def test_inactive_author_gets_teammates_only(self):
        self.assertEqual(["alice", "carol"], compute_assignees("erin", ROSTER, BUDDIES))


class SkipReasonTest(unittest.TestCase):
    def test_bots_and_automation(self):
        self.assertIsNotNone(skip_reason("github-actions[bot]"))
        self.assertIsNotNone(skip_reason("renovate[bot]"))
        self.assertIsNotNone(skip_reason("renovate"))
        self.assertIsNotNone(skip_reason("alice", "[automated] Tag Release gradle.properties Updates"))

    def test_people(self):
        self.assertIsNone(skip_reason("alice", "MCH-1: something"))


class MainTest(unittest.TestCase):
    def setUp(self):
        self.dir = tempfile.TemporaryDirectory()
        self.addCleanup(self.dir.cleanup)
        self.roster = os.path.join(self.dir.name, "roster.json")
        self.assignments = os.path.join(self.dir.name, "assignments.json")
        with open(self.roster, "w") as f:
            json.dump({"members": ROSTER}, f)
        with open(self.assignments, "w") as f:
            json.dump({"month": "2026-11", "seed": 202611, "buddies": BUDDIES}, f)

    def run_main(self, *argv, month="2026-11"):
        out, err = io.StringIO(), io.StringIO()
        with contextlib.redirect_stdout(out), contextlib.redirect_stderr(err):
            code = main(
                list(argv) + ["--roster", self.roster, "--assignments", self.assignments],
                month=month,
            )
        return code, out.getvalue(), err.getvalue()

    def test_prints_one_handle_per_line(self):
        code, out, err = self.run_main("alice")
        self.assertEqual(0, code)
        self.assertEqual("bob\ncarol\n", out)
        self.assertEqual("", err)

    def test_author_not_in_roster_is_skipped(self):
        code, out, err = self.run_main("mallory")
        self.assertEqual((0, ""), (code, out))
        self.assertIn("mallory is not in the roster", err)

    def test_bot_is_skipped(self):
        code, out, err = self.run_main("github-actions[bot]")
        self.assertEqual((0, ""), (code, out))
        self.assertIn("Skipping", err)

    def test_stale_month_warns_but_uses_assignments(self):
        code, out, err = self.run_main("alice", month="2026-12")
        self.assertEqual(0, code)
        self.assertEqual("bob\ncarol\n", out)
        self.assertIn("is for 2026-11, not 2026-12", err)

    def test_missing_assignments_fails(self):
        os.remove(self.assignments)
        code, out, err = self.run_main("alice")
        self.assertEqual(1, code)
        self.assertIn("Assignments file not found", err)


if __name__ == "__main__":
    unittest.main()
