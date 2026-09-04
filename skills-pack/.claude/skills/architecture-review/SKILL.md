---
name: architecture-review
description: Review an architecture or technical design for scalability, resiliency, maintainability, integration risk, security, operability, data integrity, and enterprise alignment.
---

# Architecture Review

Review the architecture/design referenced by `$ARGUMENTS`.

## Evaluate

- requirement fit
- separation of concerns
- coupling and cohesion
- scalability and capacity assumptions
- availability and failure modes
- resiliency patterns
- data consistency and transaction boundaries
- integration/API design
- security and trust boundaries
- identity and authorization
- secrets and sensitive data handling
- observability
- operational supportability
- deployability and rollback
- vendor/platform lock-in
- maintainability and technical debt
- testability
- backward compatibility
- enterprise standards available in the repository

## Severity

Classify findings as:
- **Blocker**
- **High**
- **Medium**
- **Low**
- **Observation**

For each finding provide:
1. evidence/reason
2. impact
3. recommendation

End with:
### Decision
Choose `Approve`, `Approve with Conditions`, or `Needs Revision`, and explain the minimum changes required.
