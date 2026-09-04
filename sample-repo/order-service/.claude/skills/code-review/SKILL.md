---
name: code-review
description: Review code or a diff for correctness, maintainability, security, performance, testing gaps, backward compatibility, and adherence to repository conventions.
---

# Code Review

Review `$ARGUMENTS`. If no target is supplied, review the current uncommitted changes when available.

## Review Priorities

1. Correctness and logic errors
2. Security vulnerabilities
3. Data loss/corruption risks
4. Concurrency/transaction issues
5. Error handling
6. Backward compatibility
7. Performance/resource issues
8. Maintainability and readability
9. Logging/observability
10. Test coverage and test quality

Do not criticize style that is already consistent with the repository unless it causes a real problem.

## Finding Format

For each actionable finding:

**[Severity] Short title**
- Location: file and relevant code area
- Issue: what is wrong
- Impact: why it matters
- Recommendation: concrete fix

Severity:
- Blocker
- High
- Medium
- Low

Then provide:

### Review Summary
### Positive Observations
### Missing Tests
### Recommendation
Choose `Approve`, `Approve with Comments`, or `Request Changes`.
