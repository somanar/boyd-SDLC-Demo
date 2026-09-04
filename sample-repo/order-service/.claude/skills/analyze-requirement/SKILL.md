---
name: analyze-requirement
description: Analyze a business or technical requirement for clarity, completeness, dependencies, risks, edge cases, and implementation impact. Use for Jira stories, feature requests, change requests, or requirement documents.
---

# Analyze Requirement

Analyze the requirement supplied in `$ARGUMENTS` or present in the conversation/repository.

## Workflow

1. Restate the requirement in one concise sentence.
2. Separate confirmed facts from assumptions.
3. Identify:
   - business objective
   - actors/users
   - functional requirements
   - non-functional requirements
   - data requirements
   - integrations/dependencies
   - security/privacy considerations
   - operational/support considerations
4. Flag ambiguity, contradictions, missing information, and untestable statements.
5. Identify impacted applications, modules, APIs, data stores, jobs, or interfaces when evidence is available.
6. List happy path, alternate paths, failure paths, and important edge cases.
7. Recommend acceptance criteria and validation approach.
8. Do not invent unavailable Jira, Confluence, or system details.

## Output

### Requirement Summary
### Confirmed Facts
### Assumptions
### Gaps / Questions
### Dependencies & Impact
### Risks
### Recommended Acceptance Criteria
### Suggested Next Steps
