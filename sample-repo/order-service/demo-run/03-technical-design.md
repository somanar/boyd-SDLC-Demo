# /generate-technical-design — PROJ-142 discount-code feature

Repo inspected: `OrderController`, `OrderService`, `OrderRepository`, `CreateOrderRequest`,
`OrderItemRequest`, `OrderResponse`, `Order`, `OrderItem`, `OrderStatus`,
`OrderNotFoundException`, `GlobalExceptionHandler`, `OrderServiceTest`.

### 1. Objective
Add discount-code support to order placement in `order-service`, satisfying PROJ-142's
acceptance criteria, without breaking the existing no-code order path.

### 2. Current State
- `POST /api/orders` (`OrderController.createOrder`) accepts a `CreateOrderRequest`
  (`customerId`, `items`) with no discount field.
- `OrderService.createOrder` computes `subtotal` by summing `OrderItem.lineTotal()`
  and sets `total = subtotal` unconditionally — there is no discount step.
- `Order` is an immutable value object constructed once in `OrderService.createOrder`
  and stored via `OrderRepository` (an in-memory `ConcurrentHashMap`).
- There is no discount/coupon domain model, service, or repository anywhere in the codebase.
- `OrderRepository` only supports `save` and `findById`; there is no query by `customerId`.
- `GlobalExceptionHandler` currently handles only `OrderNotFoundException` → 404.
- No validation exists today on `CreateOrderRequest`/`OrderItemRequest` (empty items,
  non-positive quantity are all currently accepted).

### 3. Proposed Solution
Introduce a new `DiscountService` responsible for looking up, validating, and applying a
discount code to a subtotal. `OrderService.createOrder` calls it once, after computing
`subtotal` and before constructing `Order`. For this ticket's scope (per user story:
a full discount-code management system is explicitly out of scope), `DiscountService`
owns a small **in-memory code table** seeded at startup (SAVE10, a $5 fixed code, and a
first-time-only code), mirroring the existing `OrderRepository` in-memory pattern already
used in this codebase — no new infrastructure/tech introduced.

`DiscountService` is also responsible for:
- percentage/fixed discount math, with an explicit `RoundingMode` (this is the exact class
  of bug that caused `INC-207` in a hypothetical prior rollout of this feature — this design
  requires `RoundingMode.HALF_UP` be specified on every `BigDecimal.divide`/scale call),
- flooring the resulting total at zero,
- expiration checking,
- first-time-customer eligibility (via a new `OrderRepository.existsByCustomerId` query),
- usage-count increment on successful application.

### 4. End-to-End Flow
1. Client calls `POST /api/orders` with `CreateOrderRequest` (now including optional `discountCode`).
2. `OrderController.createOrder` passes the request to `OrderService.createOrder` unchanged (no controller logic added — smallest change).
3. `OrderService` validates items and builds `OrderItem`s, computes `subtotal`.
4. If `discountCode` is present, `OrderService` calls `DiscountService.apply(code, subtotal, customerId)`.
5. `DiscountService` looks up the code; if absent → throws `InvalidDiscountCodeException`; if expired → throws `ExpiredDiscountCodeException`; if first-time-only and customer is not first-time → throws `DiscountNotEligibleException`.
6. On success, `DiscountService` computes the discount amount, floors the resulting total at zero, increments the code's usage counter, and returns a `DiscountResult` (amount applied, resulting total).
7. `OrderService` builds `Order` with `subtotal`, the discounted `total`, and the applied discount code/amount, and persists it via `OrderRepository.save`.
8. `GlobalExceptionHandler` maps the new discount exceptions to 4xx responses with a clear message per exception type.
9. `OrderController` returns `201 Created` with `OrderResponse`, now including discount fields, or a 4xx error body.

### 5. Application Changes
| Component | Change |
|---|---|
| `CreateOrderRequest` | add `discountCode` (nullable `String`) field + getter/setter |
| `DiscountService` (new) | code lookup, validation, math, usage tracking |
| `DiscountCode` (new, internal model) | code, type (PERCENT/FIXED), value, expiration, firstTimeOnly flag, usage count |
| `InvalidDiscountCodeException`, `ExpiredDiscountCodeException`, `DiscountNotEligibleException` (new) | distinct 4xx error types |
| `OrderService` | call `DiscountService` after subtotal calc; carry discount fields into `Order` |
| `OrderRepository` | add `existsByCustomerId(String)` to support first-time-customer check |
| `Order` | add `appliedDiscountCode` (nullable), `discountAmount` (`BigDecimal`, defaults to `ZERO`) |
| `OrderResponse` | expose `discountCode` and `discountAmount` |
| `GlobalExceptionHandler` | add handlers for the three new exception types |

### 6. API / Interface Changes
`POST /api/orders` request body gains one optional field:
```json
{
  "customerId": "cust-1",
  "items": [ ... ],
  "discountCode": "SAVE10"
}
```
Omitting `discountCode` is fully backward compatible — behavior is identical to today.

`OrderResponse` gains two optional fields:
```json
{
  "id": "...",
  "customerId": "...",
  "subtotal": 50.00,
  "discountCode": "SAVE10",
  "discountAmount": 5.00,
  "total": 45.00,
  "status": "CREATED"
}
```
When no code is applied, `discountCode` is `null` and `discountAmount` is `0.00`.

Error responses (new, via `GlobalExceptionHandler`):
- `400 Bad Request` — `{"error": "Discount code 'X' is not valid."}` (invalid)
- `400 Bad Request` — `{"error": "Discount code 'X' has expired."}` (expired)
- `400 Bad Request` — `{"error": "Discount code 'X' is not eligible for this order."}` (not first-time)

No versioning change needed — this is an additive, backward-compatible contract change.

### 7. Data Design
- No external database exists in this repo (`OrderRepository` is in-memory); `DiscountService`
  follows the same in-memory pattern for its code table for this ticket's scope.
- `Order` gains two fields (`appliedDiscountCode`, `discountAmount`); both nullable/zero-defaulted,
  so existing in-memory orders created before this change are unaffected (no migration needed
  since the store is in-memory and not persisted across restarts).
- Usage-count storage: an `AtomicInteger` per `DiscountCode` in the in-memory table. **Not
  durable across restarts** — flagged as an Open Decision below, since Marketing's ask
  ("see how many times each code was used") implies some durability expectation this
  ticket's scope cannot fully satisfy with the existing in-memory repository pattern.

### 8. Security
- No new authentication/authorization surface is introduced; discount application happens
  within the existing (already-unauthenticated) `POST /api/orders` path.
- Error messages distinguish invalid vs. expired vs. ineligible, which is a minor
  enumeration risk (an attacker could probe for which codes exist by testing many strings).
  This is called out as an Open Decision — the alternative is a single generic
  "discount code could not be applied" message, at the cost of a worse customer experience
  for the expired case. Recommend generic message for invalid vs. specific message only for
  expired sits with Product/Security to decide; this design defaults to specific messages
  per the acceptance criteria's explicit requirement to distinguish them.
- Discount codes must **not** be logged alongside `customerId` at INFO, to avoid compounding
  the existing PII-logging gap already present in `OrderService` (see `/security-review`, step 7).
- No secrets, credentials, or external calls are introduced.

### 9. Error Handling & Resiliency
- All discount failures are synchronous, in-process exceptions mapped to 4xx — no retries needed since there's no external I/O.
- `OrderService.createOrder` fails fast: if a discount code is present and invalid/expired/ineligible, no `Order` is persisted (partial-order creation is not possible in the current single-threaded, in-process flow).
- Division/rounding: every `BigDecimal` operation in `DiscountService` must specify `RoundingMode.HALF_UP` explicitly — this design calls this out because the current codebase's `OrderItem.lineTotal()` uses `multiply` (no rounding needed there), but percentage discounts require `divide`, which throws `ArithmeticException` without a rounding mode. This is the design-level control that directly prevents `INC-207`.

### 10. Observability
- Log discount **application outcome** (code matched a known type, applied/rejected) at INFO,
  but do not include `customerId` in the same log line (see Security).
- Log rejection reason (invalid/expired/ineligible) at INFO for support diagnosis, without
  logging the full customer/order context.
- No new metrics/dashboards exist in this repo today (no metrics library present) — flagged as `TBD` for a follow-up ticket if usage-count reporting is required.

### 11. Testing Strategy
- Unit tests for `DiscountService`: valid percent, valid fixed, floor-at-zero, invalid code, expired code, first-time-only rejection, 100%-off/rounding edge case, no-code passthrough.
- Unit tests for `OrderService`: discount wired through end-to-end, existing happy-path test unaffected.
- No integration/contract tests exist in this repo currently (no `@SpringBootTest` present) — out of scope to introduce a new test tier for this ticket; flagged as a gap.
- QA test cases produced separately in step 9 of this demo, traced to the acceptance criteria in `02-user-story.md`.

### 12. Deployment & Rollback
- No feature flag infrastructure exists in this repo. Given the additive, backward-compatible
  nature of the change (discount code is optional), this design proceeds without a flag; a
  broken deploy can be rolled back by reverting the release, since no data migration occurs
  (in-memory store resets on restart/redeploy regardless).
- No database migration required.

### 13. Risks / Tradeoffs
- In-memory discount-code table means codes cannot be updated without a redeploy — acceptable for this ticket's stated scope (management UI/API is explicitly out of scope) but a real limitation.
- Usage counts do not survive a restart — acceptable short-term, flagged for Marketing/Product to confirm.
- Distinguishing invalid vs. expired in error messages is a minor enumeration risk vs. UX benefit tradeoff (see Security section).

### 14. Open Decisions
1. **Floor-at-zero confirmation**: does a fixed discount exceeding the subtotal floor the total at $0.00, or should the order instead be rejected outright? This design assumes floor-at-zero per Finance's "never below zero" wording, but Product has not explicitly confirmed which behavior they want.
2. **Usage-count durability**: is an in-memory, restart-losing counter acceptable for Marketing's reporting need, or does this require a durable store (out of scope for this ticket)?
3. **Error-message specificity**: keep distinct invalid/expired/ineligible messages (better UX, minor enumeration risk) vs. one generic message (safer, worse UX)?
4. **Discount code source of truth**: in-memory seed table is proposed for this ticket only; a real code-management capability is a separate, larger effort.

### 15. Implementation Plan
1. Add `discountCode` to `CreateOrderRequest`.
2. Add `DiscountCode` model + seed table (SAVE10 10%, a $5 fixed code, one first-time-only code) inside `DiscountService`.
3. Implement `DiscountService.apply(code, subtotal, customerId)` with explicit rounding and zero-floor.
4. Add `existsByCustomerId` to `OrderRepository`.
5. Add the three new exception types and wire into `GlobalExceptionHandler`.
6. Extend `Order` and `OrderResponse` with discount fields.
7. Wire `DiscountService` into `OrderService.createOrder`.
8. Add unit tests (step 8 of this demo).
