# PROJ-142 — Apply discount code at order placement

**Type:** Story
**Priority:** High
**Reporter:** Product (Priya)
**Component:** order-service

## Description (as written by product — deliberately rough)

Customers should be able to enter a discount code when they place an order so
they get money off. Marketing wants to run a "SAVE10" campaign next month
(10% off) and also some fixed-dollar codes like "$5 OFF". Codes can expire.
Some codes are only for first-time customers. If the code is bad we should
tell them. Make sure it works with the mobile app too.

## Notes captured in refinement

- Finance: a discount must never make the order total go below zero.
- Marketing: wants to see how many times each code was used.
- One code per order for now.

## Acceptance (rough)

- Valid code reduces the total.
- Invalid/expired code is rejected with a clear message.

> This ticket is intentionally ambiguous. `/analyze-requirement` and
> `/create-user-story` sharpen it before any code is written.
