---
name: generate-unit-tests
description: Generate or improve focused unit tests for supplied code, covering normal behavior, boundaries, errors, regressions, and important edge cases using the project's existing test framework.
---

# Generate Unit Tests

Create tests for `$ARGUMENTS`.

## Instructions

1. Inspect the implementation and existing neighboring tests.
2. Use the repository's current test framework, conventions, fixtures, and mocking approach.
3. Test observable behavior rather than private implementation details.
4. Cover:
   - happy path
   - boundary values
   - invalid inputs
   - exceptions/failure paths
   - null/empty/missing data as applicable
   - important branches
   - regression scenario for the requested change
5. Keep tests deterministic.
6. Avoid network calls, production services, real credentials, and time-dependent behavior unless properly isolated.
7. Use clear test names describing behavior.
8. Do not inflate coverage with meaningless assertions.

Run the relevant tests if execution is available.

## Output

### Tests Added / Updated
### Scenarios Covered
### Test Result
### Remaining Coverage Gaps
