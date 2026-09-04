---
name: generate-technical-design
description: Create an implementation-oriented technical design from requirements and repository context, covering components, flows, APIs, data, security, observability, testing, rollout, and open decisions.
---

# Generate Technical Design

Create a technical design for `$ARGUMENTS`.

## First
Inspect relevant repository files and available requirement context before proposing changes. Prefer established project patterns over introducing new technologies.

## Design Sections

### 1. Objective
### 2. Current State
Describe only what can be verified.

### 3. Proposed Solution
Explain components and responsibilities.

### 4. End-to-End Flow
Number the request/event/data flow.

### 5. Application Changes
Identify modules/classes/services expected to change.

### 6. API / Interface Changes
Include endpoints, contracts, events, schemas, validation, versioning, and compatibility where relevant.

### 7. Data Design
Cover entities, tables, indexes, migration, retention, and rollback where relevant.

### 8. Security
Authentication, authorization, secrets, sensitive data, input validation, auditability.

### 9. Error Handling & Resiliency
Timeouts, retries, idempotency, circuit-breaking, degraded behavior, recovery.

### 10. Observability
Logs, metrics, traces, dashboards, alerts.

### 11. Testing Strategy
Unit, integration, contract, regression, performance, security.

### 12. Deployment & Rollback
Feature flags, migrations, sequencing, backward compatibility, rollback.

### 13. Risks / Tradeoffs
### 14. Open Decisions
### 15. Implementation Plan

Label assumptions explicitly. Do not represent a proposed design as an existing architecture.
