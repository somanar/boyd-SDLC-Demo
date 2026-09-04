---
name: generate-code
description: Implement a requested code change using existing repository conventions, with validation, error handling, tests, and a concise change summary.
---

# Generate Code

Implement `$ARGUMENTS`.

## Rules

1. Inspect the relevant code, tests, configuration, and nearby patterns first.
2. Follow existing language, framework, naming, architecture, and dependency conventions.
3. Make the smallest coherent change that satisfies the requirement.
4. Preserve backward compatibility unless the requirement explicitly changes behavior.
5. Validate inputs at appropriate boundaries.
6. Handle errors deliberately; do not silently swallow failures.
7. Do not hardcode secrets, credentials, environment-specific URLs, or tokens.
8. Add or update automated tests for changed behavior.
9. Avoid unrelated refactoring.
10. Do not deploy, merge, push, or change production resources unless explicitly requested.

## Before Finishing

- run the most relevant available tests/checks
- inspect failures caused by the change
- identify anything not validated

## Output

### Implemented
### Files Changed
### Validation Performed
### Remaining Risks / Follow-ups
