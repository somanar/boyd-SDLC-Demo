# /generate-code — PROJ-142 discount-code implementation

Implements the design in `03-technical-design.md`, as revised by the Approve-with-Conditions
findings in `04-architecture-review.md` (centralized, explicitly-rounded percentage math).

### Implemented
- `discountCode` accepted as an optional field on `POST /api/orders`.
- Percentage codes (e.g. `SAVE10`, 10%) and fixed-amount codes (e.g. `5OFF`, $5.00).
- Discounts floor the order total at $0.00 rather than going negative.
- Expired-code and unknown-code rejection with distinct 400 error messages.
- First-time-customer-only code eligibility check (`WELCOME15`), using a new
  `OrderRepository.existsByCustomerId` query.
- Per-code usage counting (in-memory `AtomicInteger`, non-durable — see Open Decision #2
  in the technical design).
- All percentage math routes through one method (`DiscountService.calculateDiscountAmount`)
  that always specifies scale and `RoundingMode.HALF_UP`, so a 100%-off code cannot throw
  `ArithmeticException` — this directly addresses the architecture-review's High finding
  and is the fix for the defect class behind `INC-207`.
- No-code order creation is byte-for-byte unchanged in behavior (backward compatible).

### Files Changed

**New**
- `src/main/java/.../model/DiscountType.java`
- `src/main/java/.../model/DiscountCode.java`
- `src/main/java/.../model/DiscountResult.java`
- `src/main/java/.../exception/InvalidDiscountCodeException.java`
- `src/main/java/.../exception/ExpiredDiscountCodeException.java`
- `src/main/java/.../exception/DiscountNotEligibleException.java`
- `src/main/java/.../service/DiscountService.java`

**Modified**
- `src/main/java/.../dto/CreateOrderRequest.java` — add optional `discountCode`.
- `src/main/java/.../dto/OrderResponse.java` — expose `discountCode`, `discountAmount`.
- `src/main/java/.../model/Order.java` — add `appliedDiscountCode`, `discountAmount`.
- `src/main/java/.../repository/OrderRepository.java` — add `existsByCustomerId`.
- `src/main/java/.../service/OrderService.java` — call `DiscountService` when a code is present.
- `src/main/java/.../exception/GlobalExceptionHandler.java` — map the three new
  exceptions to `400 Bad Request`.
- `src/test/java/.../service/OrderServiceTest.java` — updated the existing test's
  `OrderService` construction for the new `DiscountService` constructor dependency
  (required for compilation; no behavior/assertions changed).

### Validation Performed
```
mvn -q test
```
Result: **BUILD SUCCESS** — the one existing test (`createsOrderWithComputedSubtotal`)
still passes unmodified in its assertions, confirming the no-discount path is unaffected.

New behavior (discount paths) is not yet covered by automated tests at this stage —
that is the explicit purpose of `/generate-unit-tests` in step 8 of this demo. This
change was deliberately left uncommitted so `/code-review` (step 6) and
`/security-review` (step 7) review it as "current uncommitted changes," per the runbook.

### Remaining Risks / Follow-ups
- Discount codes are seeded in-memory in `DiscountService` (`SAVE10`, `5OFF`,
  `WELCOME15`) — there is no code-management API; adding/removing codes requires a
  redeploy (accepted scope limitation, see technical design Open Decision #4).
- Usage counts (`DiscountService.usageCount`) are not exposed via any API yet and do
  not survive a restart (Open Decision #2) — flagged for Product/Marketing sign-off.
- No new input validation was added to `CreateOrderRequest`/`OrderItemRequest` beyond
  the discount code itself (empty carts, non-positive quantities remain unvalidated) —
  that is a pre-existing gap out of scope for this ticket, per "avoid unrelated
  refactoring"; it is surfaced separately by `/security-review` in step 7.
- No unit tests yet for the new discount logic — addressed in step 8.
