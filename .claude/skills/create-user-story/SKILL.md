---
name: create-user-story
description: Convert a requirement or feature idea into a well-structured Jira-style user story with acceptance criteria, dependencies, risks, and definition-of-done items.
---

# Create User Story

Convert `$ARGUMENTS` or the supplied requirement into an implementation-ready user story.

## Instructions

Use this format:

### Title
Short outcome-focused title.

### User Story
As a [persona],
I want [capability],
so that [business value].

### Business Context
Explain why the change is needed.

### Scope
**In scope**
- ...

**Out of scope**
- ...

### Acceptance Criteria
Use Given / When / Then where practical.
Include positive, negative, validation, authorization, integration, and error-handling scenarios.

### Non-Functional Requirements
Cover performance, security, logging, observability, availability, accessibility, and data handling only where applicable.

### Dependencies
List known technical/business dependencies. Mark unknown dependencies as TBD.

### Risks / Assumptions
Keep facts and assumptions separate.

### Definition of Done
Include implementation, code review, automated tests, security checks, documentation, and deployment/rollback readiness as applicable.

Do not fabricate identifiers, system names, owners, or deadlines.
