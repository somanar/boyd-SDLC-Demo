# /create-user-story — PROJ-142

Input: PROJ-142 requirement + gaps identified in `01-analyze-requirement.md`.

### Title
Apply a discount code at order placement

### User Story
As a customer placing an order,
I want to enter a discount code at checkout,
so that I receive the promotional savings marketing is offering (e.g. SAVE10, $5 OFF) without paying full price.

### Business Context
Marketing needs to run promotional campaigns (percentage-off and fixed-dollar-off codes) to drive order volume, starting with a "SAVE10" (10%) campaign next month. Finance requires the discount can never drop an order below zero, and Marketing wants usage counts per code to measure campaign performance. The order path is shared by web and mobile, so the fix must live in `order-service`, not a client.

### Scope

**In scope**
- Accept an optional discount code on order creation (`POST /api/orders`).
- Support percentage-off codes (e.g. 10%) and fixed-dollar-off codes (e.g. $5).
- Reject invalid or expired codes with a clear, distinct error message.
- Reject a code restricted to first-time customers when the customer is not first-time.
- Enforce one code per order.
- Ensure the discounted total is never negative (floored at zero).
- Expose the applied discount (code and amount) on the order response so both web and mobile clients can display it.
- Increment a per-code usage counter when a code is successfully applied.

**Out of scope**
- Admin UI or API for creating/managing discount codes.
- Stacking multiple codes on one order.
- Discount-code analytics/reporting dashboards (usage counts are tracked, not surfaced in a report).
- Historical/customer order-history persistence beyond what's needed to evaluate "first-time customer."

### Acceptance Criteria

**Valid code — percentage off**
Given an active order with a subtotal of $50.00
And a valid, unexpired percentage code "SAVE10" (10% off)
When the customer places the order with that code
Then the order total is $45.00
And the response includes the applied discount code and discount amount.

**Valid code — fixed amount**
Given an active order with a subtotal of $20.00
And a valid, unexpired fixed-amount code "$5 OFF" ($5.00 off)
When the customer places the order with that code
Then the order total is $15.00.

**Discount larger than subtotal (floor at zero)**
Given an active order with a subtotal of $3.00
And a valid fixed-amount code worth $5.00
When the customer places the order with that code
Then the order total is $0.00, not a negative number.

**Invalid code**
Given an order in progress
And a discount code that does not exist
When the customer places the order with that code
Then the order is rejected with a 4xx response and a message identifying the code as invalid
And no order is created.

**Expired code**
Given an order in progress
And a discount code that exists but is past its expiration date
When the customer places the order with that code
Then the order is rejected with a 4xx response and a message distinguishing "expired" from "invalid"
And no order is created.

**First-time-customer-only code, returning customer**
Given a customer who has placed at least one prior order
And a discount code restricted to first-time customers
When the customer places a new order with that code
Then the order is rejected with a 4xx response and a message explaining the eligibility restriction.

**No discount code supplied**
Given an order in progress with no discount code
When the customer places the order
Then behavior is unchanged from today: total equals subtotal.

**Usage counting**
Given a valid code is successfully applied to an order
When the order is created
Then that code's usage counter is incremented by exactly one.

### Non-Functional Requirements
- **Security**: discount-code validation must not leak whether a code exists vs. is merely ineligible in a way that helps enumerate valid codes (align with `/security-review` findings on this repo).
- **Logging**: do not log discount codes alongside customer identifiers at INFO in a way that compounds the existing PII-in-logs issue already present in `OrderService` (see repo README seeded gaps).
- **Performance**: discount lookup/validation must not introduce unbounded latency to the synchronous order-creation path.
- **Data handling**: discount amount and code must be part of the persisted `Order` so it is auditable after the fact.

### Dependencies
- A source of truth for discount codes (definition, percentage/amount, expiration, first-time-only flag) — **TBD**, not defined in the current ticket or repo.
- A way to determine "first-time customer" — **TBD**, `OrderRepository` has no customer-history query today.
- Definition of the API error contract (status code, body shape) for invalid/expired codes — **TBD**.

### Risks / Assumptions
- **Assumption**: codes are case-insensitive and trimmed before matching (not confirmed by Product).
- **Assumption**: "first-time customer" is inferred from the in-memory order store having no prior order for that `customerId` (best available signal in this codebase; a real customer-history system may differ).
- **Risk**: percentage-based math on `BigDecimal` must use an explicit rounding mode; the incident file used later in this demo (`INC-207`) is a direct real-world instance of getting this wrong for a 100%-off code.
- **Risk**: without a defined source of truth for codes, the initial implementation must decide between hardcoding a small in-memory code table (acceptable for this ticket's scope) vs. a full store (out of scope) — flagged for design in the next step.

### Definition of Done
- Implementation merged behind the existing `POST /api/orders` endpoint with no breaking change when no code is supplied.
- Code reviewed (`/code-review`) and security-reviewed (`/security-review`) with no unresolved Blocker/Critical/High findings.
- Automated unit tests cover: valid percentage code, valid fixed code, floor-at-zero, invalid code, expired code, first-time-only restriction, no-code baseline, and the 100%-off/rounding edge case.
- QA test cases produced and traced to these acceptance criteria.
- Documentation updated to describe the discount-code capability and its known limitations (e.g., code source is TBD).
- Rollback path confirmed: the feature can be disabled/reverted without affecting non-discounted order creation.
