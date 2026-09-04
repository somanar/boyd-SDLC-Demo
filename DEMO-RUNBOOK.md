# Demo Runbook — Claude across the full SDLC

**Audience:** the software development team
**Format:** live walkthrough in Claude Code, one feature ticket carried end to end
**Length:** ~35–45 min (or run the 15-min "highlight reel" — see the end)
**Sample project:** `sample-repo/order-service` (Spring Boot)
**Story:** ticket **PROJ-142** — "apply a discount code at order placement"

The idea: instead of showing 12 disconnected tricks, we take *one* real ticket
and walk it through the entire lifecycle. Each skill's output becomes the next
skill's input — the way real work actually flows.

---

## The narrative arc

| # | SDLC stage | Skill | What the audience sees |
|---|---|---|---|
| 1 | Requirements | `/analyze-requirement` | A rough ticket pulled apart into facts, gaps, risks |
| 2 | Backlog | `/create-user-story` | A clean, testable user story with acceptance criteria |
| 3 | Design | `/generate-technical-design` | An implementation design grounded in the real repo |
| 4 | Design review | `/architecture-review` | The design graded, with a go/needs-revision call |
| 5 | Build | `/generate-code` | The discount feature implemented in the codebase |
| 6 | Peer review | `/code-review` | The diff reviewed for correctness & maintainability |
| 7 | Security | `/security-review` | Auth, PII-logging, and validation gaps caught |
| 8 | Unit tests | `/generate-unit-tests` | Real JUnit tests for the new logic and old gaps |
| 9 | QA design | `/generate-test-cases` | A structured QA test matrix traced to acceptance criteria |
| 10 | Operations | `/root-cause-analysis` | A simulated prod incident diagnosed from logs |
| 11 | Docs | `/generate-documentation` | A feature/runbook doc generated from the code |
| 12 | Release | `/release-readiness-review` | A go / no-go scorecard for the release |

---

## Before you present (5 min, do this once)

1. Have **Claude Code** installed and working.
2. Open a terminal in `sample-repo/order-service`. The skills live in
   `.claude/skills/` there, so they load automatically.
3. Confirm the project builds (first build pulls dependencies):
   ```bash
   mvn -q test
   ```
   You should see the one existing test pass.
4. Keep `demo-inputs/PROJ-142-discount-code.md` and
   `demo-inputs/INC-207-incident-log.md` handy — you'll paste from them.
5. Optional but great: use `git` so you can show real diffs.
   ```bash
   git init && git add -A && git commit -m "baseline order-service"
   ```
6. **Do not pre-fix the seeded gaps** (see the repo README). They're what make
   the review and test skills produce real findings.

> Tip: run each skill, then pause and *read a bit of the output out loud*. The
> "wow" isn't that Claude typed fast — it's that the content is specific to this
> repo and this ticket.

---

## The walkthrough

Each step below gives you: the one-line **framing** to say, the **command** to
run, and **what to point at** on screen.

### 1 — `/analyze-requirement` · Requirements

**Say:** "Product handed us this ticket. It's vague, like most tickets. Before
anyone writes code, let's pull it apart."

**Run:** paste the ticket body, or point the skill at the file:
```
/analyze-requirement Review demo-inputs/PROJ-142-discount-code.md — a request to add discount-code support to order placement.
```

**Point at:** the split between **Confirmed Facts** and **Assumptions**, and the
**Gaps / Questions** (e.g. "what happens when the discount exceeds the total?",
"are stacked codes allowed?", "how are codes validated?"). Note that Claude did
*not* invent a Jira number or an owner — it flags unknowns instead.

---

### 2 — `/create-user-story` · Backlog

**Say:** "Now turn that analysis into something a developer can actually pick up."

**Run:**
```
/create-user-story Turn the PROJ-142 discount-code requirement (and the gaps we just found) into an implementation-ready story.
```

**Point at:** the **Given/When/Then acceptance criteria**, including the negative
cases (invalid code, expired code, discount larger than subtotal) that came out
of step 1, and the **Definition of Done**. This is the artifact you'd paste back
into Jira.

---

### 3 — `/generate-technical-design` · Design

**Say:** "Design time. Notice it reads the actual repo first — this isn't a
generic essay."

**Run:**
```
/generate-technical-design Design the discount-code feature for order-service per PROJ-142. Inspect the existing OrderService, controller, and DTOs first.
```

**Point at:** references to the **real classes** (`OrderService`,
`CreateOrderRequest`, `OrderResponse`), the proposed `DiscountService`, the
**API/DTO change** (new `discountCode` field), and the **Open Decisions** section
where it surfaces the below-zero and rounding questions rather than guessing.

---

### 4 — `/architecture-review` · Design review

**Say:** "Before we build, a second set of eyes on the design."

**Run:**
```
/architecture-review Review the discount-code technical design we just produced for order-service.
```

**Point at:** the **severity-graded findings** and the explicit
**Approve / Approve with Conditions / Needs Revision** decision. Great moment to
say: "this is the design-review gate, automated but reviewable."

---

### 5 — `/generate-code` · Build

**Say:** "Now we implement — following the patterns already in the repo, smallest
change that works."

**Run:**
```
/generate-code Implement PROJ-142: add discount-code support to order placement, following the approved design and existing repo conventions. Add a DiscountService, wire it into OrderService, and extend the request/response DTOs.
```

**Point at:** the **Files Changed** list and then the actual diff
(`git diff`). Call out that it followed the existing style and added the
`discountCode` field rather than rewriting everything.

> Presenter note: keep this change **uncommitted** — the next two skills review
> it as "current uncommitted changes."

---

### 6 — `/code-review` · Peer review

**Say:** "Peer review, on the change we just made."

**Run:**
```
/code-review Review the uncommitted discount-code changes.
```

**Point at:** severity-tagged findings with **file locations and concrete fixes**,
the **Missing Tests** section, and the **Approve / Approve with Comments /
Request Changes** call. If Claude critiques its own generated code, lean into it:
"the reviewer and the author being separate passes is the point."

---

### 7 — `/security-review` · Security

**Say:** "Security review — this is where the seeded weaknesses surface."

**Run:**
```
/security-review Perform a defensive security review of order-service, focusing on the order APIs and the new discount logic.
```

**Point at:** it should catch the **broken object-level authorization** on
`GET /api/orders/{id}` (any customer can read any order), the **customer id
logged at INFO**, and the **missing input validation**. Each finding has a
**verification step**. Note the remediation stays defensive.

---

### 8 — `/generate-unit-tests` · Unit tests

**Say:** "Fill the test gaps — for the new code *and* the old happy-path-only
coverage."

**Run:**
```
/generate-unit-tests Add unit tests for DiscountService and OrderService, covering the discount rules, empty carts, invalid quantities, and the below-zero / rounding edge cases.
```

**Point at:** real JUnit tests in the project's style (mirrors
`OrderServiceTest`), the **Scenarios Covered** list, and — if you run them —
the **test result**. This is also the moment a good demo *catches the bug early*:
a test for a 100%-off code is exactly what would have prevented INC-207.

**Run (optional, strong):**
```bash
mvn -q test
```

---

### 9 — `/generate-test-cases` · QA design

**Say:** "What QA would design — structured, traceable test cases, not just unit
tests."

**Run:**
```
/generate-test-cases Produce QA test cases for the PROJ-142 discount-code feature, traced to the acceptance criteria.
```

**Point at:** the **test-case table** (ID, steps, data, expected, priority, type)
and the **Traceability to Acceptance Criteria** section that ties each case back
to step 2's story. Note the **Automation Candidates** column.

---

### 10 — `/root-cause-analysis` · Operations

**Say:** "Fast-forward: it's in prod and something's wrong. Here's the incident."

**Run:** paste from the incident file, or:
```
/root-cause-analysis Diagnose INC-207 using demo-inputs/INC-207-incident-log.md — some discounted checkouts return HTTP 500 after the v0.2.0 rollout.
```

**Point at:** the **evidence-based timeline**, the separation of **evidence vs
hypotheses**, and the landing on the likely cause (a percentage-discount division
without a rounding mode, tripped only by certain values). Close the loop:
"steps 6–9 are the gates designed to stop exactly this from reaching prod."

---

### 11 — `/generate-documentation` · Docs

**Say:** "Docs, generated from the code that now exists — not from memory."

**Run:**
```
/generate-documentation Write feature + support documentation for the discount-code capability in order-service, including a short runbook section.
```

**Point at:** sections grounded in the real implementation, a **Mermaid diagram**
if it includes one, and `TBD` markers where operational details (owners, dashboard
URLs) genuinely aren't known — it doesn't fabricate them.

---

### 12 — `/release-readiness-review` · Release

**Say:** "Finally — are we actually ready to ship?"

**Run:**
```
/release-readiness-review Assess release readiness for order-service v0.2.0 (the discount-code change), using everything we produced in this session.
```

**Point at:** the **✅/⚠️/❌/❓ scorecard**, the **Release Blockers**, and the
**Go / No-Go** recommendation. Perfect closing line: "one ticket, twelve stages,
every artifact a real engineer would produce — reviewed, tested, documented, and
gated."

---

## Timing

| Segment | Steps | ~min |
|---|---|---|
| Plan & design | 1–4 | 12 |
| Build & review | 5–7 | 12 |
| Test | 8–9 | 8 |
| Operate & ship | 10–12 | 10 |

## 15-minute highlight reel

If you have a short slot, run **1 → 2 → 5 → 6 → 7 → 10**: rough ticket becomes a
clean story, gets built, gets reviewed, security catches the seeded gaps, and RCA
ties an incident back to a missed check. That arc lands on its own.

## If something goes sideways

- **A skill doesn't trigger:** invoke it explicitly with the leading slash and a
  clear target, e.g. `/code-review src/`. Confirm you launched Claude Code from
  `sample-repo/order-service` so `.claude/skills/` is in scope.
- **Output is too generic:** add "inspect the actual repo first" to the prompt.
  The design/code skills are strongest when pointed at real files.
- **The build can't fetch dependencies:** run the demo somewhere with normal
  internet; the first Maven build downloads Spring Boot.
- **You're short on time mid-demo:** skip to step 10 (RCA) — it stands alone and
  is a strong finish.

## What to emphasize to the team

- These skills are **guardrails, not autopilot**: they inspect before changing,
  separate facts from assumptions, keep humans in the review/approve seat, and
  never deploy or expose secrets.
- Every output is an artifact your process **already expects** — a story, a
  design, a review, tests, an RCA — just produced faster and more consistently.
- They're **yours to customize**: the placeholders in
  `references/boyd-enterprise-standards.md` are where your real coding, security,
  and CI/CD standards go (see the top-level `README.md`).
