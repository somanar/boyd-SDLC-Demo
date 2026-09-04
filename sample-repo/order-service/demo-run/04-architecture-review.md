# /architecture-review — PROJ-142 technical design

Reviewing `03-technical-design.md`.

Note: `references/boyd-enterprise-standards.md` in this repo is an unfilled placeholder
template (no Boyd-specific standards have been added yet), so this review cannot check
alignment against real enterprise standards — flagged as an Observation below rather than
silently skipped.

### Findings

**[High] Discount math must specify rounding mode explicitly**
- Evidence: Section 9 of the design calls out that `DiscountService` will use `BigDecimal.divide` for percentage math, and explicitly requires `RoundingMode.HALF_UP` on every such call.
- Impact: If this requirement is not enforced in code review, a 100%-off (or other edge-value) percentage discount throws `ArithmeticException: Rounding necessary` at runtime — this is not hypothetical, it is the exact defect this demo's incident (`INC-207`) is built around.
- Recommendation: Treat "explicit rounding mode on every `BigDecimal.divide`" as a hard gate in `/code-review`, not just a design note. Consider a single private helper method in `DiscountService` that centralizes all percentage math so the rounding mode can't be forgotten in a second call site later.

**[Medium] Error-message specificity is an enumeration risk left as an Open Decision**
- Evidence: Section 8 (Security) and Open Decision #3 both flag that distinguishing "invalid" vs. "expired" vs. "ineligible" in error responses lets an attacker probe for which codes exist.
- Impact: Low-severity information disclosure — code enumeration, not account or payment compromise. Acceptable for a promotional-code system, but worth a conscious sign-off rather than defaulting silently.
- Recommendation: Approve as designed (distinct messages) given the acceptance criteria explicitly require distinguishing expired from invalid, but require this tradeoff be stated in the release notes/security review rather than left implicit.

**[Medium] Usage-count durability does not meet the implied business need**
- Evidence: Design section 7 and Open Decision #2 state usage counts are held in an `AtomicInteger` in an in-memory table that resets on restart/redeploy.
- Impact: Marketing's stated goal ("see how many times each code was used") will silently produce wrong/reset numbers across deploys, which is worse than not having the feature if numbers are later reported as authoritative.
- Recommendation: Approve for this ticket's scope *only if* Product/Marketing explicitly accepts non-durable counts as a known limitation before this ships. This should not be a silent implementation detail — it belongs in the release notes and in `/generate-documentation` output.

**[Low] No integration/contract test tier exists for the new discount error responses**
- Evidence: Section 11 notes no `@SpringBootTest`-level tests exist in the repo and none are being added for this ticket.
- Impact: The new 4xx error contract (three new exception → HTTP mappings) is only unit-tested at the service layer, not verified end-to-end through `GlobalExceptionHandler` and the actual HTTP response body.
- Recommendation: Acceptable to defer given the repo has no existing integration-test precedent (design correctly avoids introducing a new test tier unilaterally), but flag as a backlog item, not a blocker for this ticket.

**[Observation] Enterprise standards reference file is an unfilled placeholder**
- Evidence: `references/boyd-enterprise-standards.md` contains only `[ADD]` placeholders for architecture, security, API, and testing standards.
- Impact: This review (and every other skill in this demo) cannot verify alignment to real organizational standards because none have been supplied yet.
- Recommendation: Not a defect in this design — flagged so the team knows to populate that file before relying on these skills for real enterprise-standards conformance checks (see `references/customization-example.md`).

**[Observation] In-memory discount-code table matches existing repo conventions**
- Evidence: `OrderRepository` already uses an in-memory `ConcurrentHashMap`; the design's `DiscountService` code table follows the same pattern rather than introducing new infrastructure (e.g. a database) unprompted.
- Impact: Positive — this keeps the change minimal and consistent with the existing architecture, per the "smallest coherent change" principle.
- Recommendation: None; note as a strength.

### Decision
**Approve with Conditions.**

Minimum changes required before implementation proceeds:
1. Centralize all discount percentage math through one method that enforces `RoundingMode.HALF_UP` (or equivalent), so the rounding-mode requirement cannot be missed at a second call site.
2. Get explicit Product/Marketing sign-off (even informal, in the PR description) that non-durable usage counts are acceptable for this release; state it in the documentation produced in step 11.
3. Keep the distinct invalid/expired/ineligible error messages as designed, but note the enumeration tradeoff in the security review (step 7) rather than treating it as resolved.

No blockers — the design is grounded in the real repo, keeps the change minimal, and does not introduce new infrastructure. Proceed to `/generate-code`.
