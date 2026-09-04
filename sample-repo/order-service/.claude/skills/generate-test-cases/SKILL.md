---
name: generate-test-cases
description: Turn requirements, acceptance criteria, designs, or code changes into structured QA test cases covering functional, negative, boundary, integration, regression, security, and operational scenarios.
---

# Generate Test Cases

Generate test cases for `$ARGUMENTS`.

## Coverage

Include applicable:
- functional happy path
- alternate paths
- negative cases
- boundary conditions
- validation
- role/permission behavior
- API/integration behavior
- data persistence/integrity
- error handling
- retries/idempotency
- regression risks
- security checks
- logging/monitoring verification
- performance scenarios
- rollback/recovery scenarios

## Table Format

| ID | Scenario | Preconditions | Steps | Test Data | Expected Result | Priority | Type |
|---|---|---|---|---|---|---|---|

Priorities: P0, P1, P2, P3.
Types: Functional, Negative, Boundary, Integration, Regression, Security, Performance, Operational.

After the table include:
### Traceability to Acceptance Criteria
### Test Data / Environment Needs
### Automation Candidates
### Gaps / Clarifications
