# order-service (demo sample)

A deliberately small Spring Boot REST service used to demonstrate Claude's
SDLC skills. It is real, compilable code — not pseudo-code — but it is seeded
with a few realistic gaps so the review and generation skills have genuine work
to do.

## Run it

```bash
mvn spring-boot:run
```

Then:

```bash
# create an order
curl -s -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"cust-1","items":[{"sku":"SKU-1","quantity":2,"unitPrice":10.00}]}'

# fetch an order (use the id returned above)
curl -s http://localhost:8080/api/orders/<id>
```

> The build downloads Spring Boot from Maven Central the first time, so run it
> somewhere with normal internet access (not an air-gapped box).

## What's in the box

| Layer | Class |
|---|---|
| Entry point | `OrderServiceApplication` |
| API | `OrderController` — `POST /api/orders`, `GET /api/orders/{id}` |
| Logic | `OrderService` |
| Storage | `OrderRepository` (in-memory) |
| Model | `Order`, `OrderItem`, `OrderStatus` |
| DTOs | `CreateOrderRequest`, `OrderItemRequest`, `OrderResponse` |
| Errors | `OrderNotFoundException`, `GlobalExceptionHandler` |
| Tests | `OrderServiceTest` (intentionally thin) |

## Seeded gaps (don't "fix" these before the demo)

These exist so the skills produce real findings live. See
`DEMO-RUNBOOK.md` for how each is surfaced.

1. **No discount support.** `CreateOrderRequest` has no coupon field and
   `total` just mirrors `subtotal`. Building this is the feature ticket
   **PROJ-142**, carried through every skill.
2. **Missing input validation.** Empty carts, null items, and non-positive
   quantities are not rejected. → `/code-review`, `/security-review`
3. **Sensitive data in logs.** `OrderService` logs the customer identifier at
   INFO. → `/security-review`
4. **Broken object-level authorization (IDOR).** `GET /api/orders/{id}`
   returns any order with no ownership check. → `/security-review`
5. **Thin tests.** Only one happy-path test; no edge/negative coverage.
   → `/generate-unit-tests`

## Skills

The Boyd AI SDLC skills are installed under `.claude/skills/`. Start Claude
Code from this directory and invoke, e.g., `/code-review src/`.
