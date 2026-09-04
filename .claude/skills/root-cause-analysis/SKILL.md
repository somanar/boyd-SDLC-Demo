---
name: root-cause-analysis
description: Analyze an incident, defect, logs, or production symptom to build an evidence-based timeline, identify likely root cause and contributing factors, and recommend corrective and preventive actions.
---

# Root Cause Analysis

Analyze `$ARGUMENTS`.

## Rules

- Treat correlation as different from causation.
- Separate evidence, hypotheses, and unknowns.
- Prefer logs, metrics, traces, code, configuration, deployment history, and reproducible behavior.
- Do not assign blame to individuals.

## Output

### Incident Summary
Impact, affected capability, duration if known.

### Timeline
Chronological evidence.

### Evidence
What is directly supported.

### Hypotheses Considered
For each: supporting evidence, contradicting evidence, confidence.

### Root Cause
State only if sufficiently supported. Otherwise say `Most likely cause` and confidence level.

### Contributing Factors
Process, design, monitoring, testing, operational, dependency factors.

### Immediate Remediation
Actions to restore/stabilize service.

### Corrective Actions
Permanent fixes.

### Preventive Actions
Tests, alerts, controls, architecture/process improvements.

### Verification
How to prove the issue is resolved and unlikely to recur.
