---
name: release-readiness-review
description: Assess whether a software change is ready for release by checking requirements, code quality, testing, security, dependencies, observability, deployment, rollback, documentation, and operational support.
---

# Release Readiness Review

Assess `$ARGUMENTS`.

## Checklist

### Requirements
- acceptance criteria satisfied
- scope understood
- unresolved requirement gaps identified

### Code Quality
- code review complete
- critical/high findings resolved
- technical debt acknowledged

### Testing
- unit tests
- integration/contract tests
- regression testing
- performance testing if required
- evidence/results available

### Security
- security review/scans completed
- secrets/dependency issues addressed
- access/authorization validated

### Data / Integration
- migrations validated
- compatibility considered
- downstream/upstream impact assessed

### Observability
- logs
- metrics
- alerts
- dashboards
- correlation/traceability

### Deployment
- deployment steps
- configuration
- feature flags
- sequencing
- rollback plan

### Operations
- runbook
- support ownership
- known issues
- incident/escalation path

### Documentation
- technical and user/support documentation updated

## Output

Use:
- ✅ Ready
- ⚠️ Conditional
- ❌ Not Ready
- ❓ Unknown

Provide:

### Readiness Scorecard
### Release Blockers
### Conditions / Follow-ups
### Go / No-Go Recommendation

Never execute a deployment as part of this skill.
