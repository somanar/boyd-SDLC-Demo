# /analyze-requirement — PROJ-142

Source: `demo-inputs/PROJ-142-discount-code.md`

### Requirement Summary
Allow customers to apply a discount code (percentage-off or fixed-dollar-off) when placing an order in `order-service`, so marketing can run promotional campaigns; invalid, expired, or unusable codes must be clearly rejected, and a discount must never take the order total below zero.

### Confirmed Facts
- Customers should be able to enter a discount code at order placement.
- Marketing wants two code types at launch: a 10%-off campaign ("SAVE10") and fixed-dollar codes ("$5 OFF").
- Codes can expire.
- Some codes are restricted to first-time customers only.
- Finance requires: the discount must never make the order total go below zero.
- Marketing wants per-code usage counts.
- One discount code per order for now (no stacking).
- Invalid/expired codes must be rejected with a clear message to the customer.
- The order path is used by both the web and mobile clients ("make sure it works with the mobile app too") — implying the change must be at the API/service layer, not client-specific.

### Assumptions
- "First-time customer" means the customer has no prior orders in this system (not defined in the ticket — flagged below as a gap).
- Discount codes are case-insensitive and single-use-per-order, not single-use-globally, unless usage limits are specified later.
- Codes are configured/seeded somewhere (out of scope of this ticket to define storage, but a source of truth is needed).
- "Mobile app" consumes the same REST API (`order-service`) rather than a separate backend, since no mobile-specific service is mentioned in the repo.
- Currency/rounding follows the existing `BigDecimal` handling already used for `subtotal`/`total` in `Order`.

### Gaps / Questions
- What happens when a fixed-dollar discount exceeds the order subtotal? (Finance only says "never below zero" — does it floor at zero, or does the code fail validation entirely?)
- Are discount codes case-sensitive, and can they contain whitespace?
- Where do discount codes live — hardcoded, config, or a future admin-managed store? No such store exists in the repo today.
- How is "first-time customer" determined given `OrderRepository` is in-memory and has no customer/order-history query today?
- What is the expected HTTP status and error payload shape when a code is invalid/expired (the API currently has no error contract for this)?
- Is code matching required to be exact, or do codes need normalization (trim/uppercase)?
- Who owns usage-count tracking and where is it persisted/reported (Marketing's ask has no stated system)?
- Is there a maximum discount percentage/amount per code, and can 100%-off codes exist? (Relevant — see INC-207 later in this demo.)
- Are discount codes visible/audited anywhere for compliance (e.g., who applied "SUMMER100" and when)?

### Dependencies & Impact
- **`CreateOrderRequest`** (DTO) — needs a new `discountCode` field; currently has none.
- **`OrderService.createOrder`** — currently computes `total` as a straight mirror of `subtotal`; needs a discount-application step before persisting the order.
- **`OrderResponse`** — may need to expose the applied discount (code, amount) for client display/confirmation.
- **`OrderRepository`** — in-memory store; if "first-time customer" or per-code usage tracking is required, this store currently has no way to query by customer or code.
- No existing `DiscountService` or discount domain model exists in the repo — this is new capability, not a modification of existing logic.
- Web and mobile clients both hit the same `POST /api/orders` endpoint, so a single server-side change should satisfy both, per the existing controller (`OrderController`).

### Risks
- **Arithmetic/rounding risk**: percentage-based discounts on `BigDecimal` values must specify a rounding mode explicitly, or a 100%-off (or other edge-value) discount can throw at runtime — this is a real, live risk in this codebase pattern (see `INC-207` incident used later in this demo).
- **Negative-total risk**: without an explicit floor, a fixed-dollar discount larger than the subtotal could produce a negative total, violating the Finance constraint.
- **No validation today**: `CreateOrderRequest`/`OrderItemRequest` currently accept empty item lists and non-positive quantities unchecked, so any new discount logic sits on top of an already-unvalidated request path.
- **Silent data exposure**: `OrderService` already logs the customer identifier at INFO; adding discount-code logging must not compound this by logging codes tied to identifiable customers without review.
- **Undefined error contract**: without a defined invalid/expired-code response shape, client behavior (web vs. mobile) may diverge.

### Recommended Acceptance Criteria
- Given a valid, unexpired, applicable discount code, when an order is placed, then the discount is applied and the total reflects the discount (never below zero).
- Given an invalid or unrecognized discount code, when an order is placed, then the order is rejected with a clear, specific error message and no order is created.
- Given an expired discount code, when an order is placed, then the order is rejected with a clear, specific error message distinguishing "expired" from "invalid."
- Given a first-time-customer-only code applied by a returning customer, when an order is placed, then the code is rejected with a clear message.
- Given a fixed-dollar code whose value exceeds the subtotal, when an order is placed, then the total is floored at zero (pending confirmation — see Gaps).
- Given no discount code is supplied, when an order is placed, then behavior is unchanged from today (total mirrors subtotal).

### Suggested Next Steps
1. Resolve the open gaps above with Product/Finance before implementation, especially the below-subtotal floor behavior and first-time-customer definition.
2. Proceed to `/create-user-story` to convert this into an implementation-ready story with explicit Given/When/Then criteria.
3. Proceed to `/generate-technical-design` to define where discount codes are sourced from and how usage counts are tracked, grounded in the actual repo structure.
