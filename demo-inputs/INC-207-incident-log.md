# INC-207 — Some checkouts failing after discount rollout

**Reported:** 2026-09-03 14:22 CT by on-call
**Symptom:** A subset of orders using a discount code return HTTP 500. Customers
report the app says "something went wrong" at the final Pay step.

## Deploy history

- 2026-09-03 13:40 CT — order-service `v0.2.0` deployed to prod (adds PROJ-142
  discount support).
- No config or infra changes in the window.

## Log excerpt (order-service, prod)

```
13:41:02 INFO  OrderService - Creating order for customer cust-8831 with 1 items
13:41:02 INFO  DiscountService - Applying code SAVE10 (percent=10)
13:41:02 INFO  OrderService - Order created total=17.99
13:58:44 INFO  OrderService - Creating order for customer cust-9002 with 3 items
13:58:44 INFO  DiscountService - Applying code SUMMER100 (percent=100)
13:58:44 ERROR OrderService - Unhandled error creating order
  java.lang.ArithmeticException: Rounding necessary
     at java.base/java.math.BigDecimal.divide(BigDecimal.java:...)
     at com.boyd.demo.orderservice.service.DiscountService.apply(DiscountService.java:...)
     at com.boyd.demo.orderservice.service.OrderService.createOrder(OrderService.java:...)
14:05:10 INFO  DiscountService - Applying code SAVE10 (percent=10)
14:05:10 INFO  OrderService - Order created total=42.30
14:19:37 INFO  DiscountService - Applying code FREEALL (percent=100)
14:19:37 ERROR OrderService - Unhandled error creating order
  java.lang.ArithmeticException: Rounding necessary
```

## Observations from support

- Orders with 10% codes seem fine.
- 100%-off promo codes ("SUMMER100", "FREEALL") consistently fail.

> Feed this to `/root-cause-analysis` during the demo. (The trap: a percentage
> discount path that divides without a rounding mode, only tripped by certain
> values — a classic "works in the demo, fails in prod" defect that the earlier
> review/testing skills are designed to catch first.)
