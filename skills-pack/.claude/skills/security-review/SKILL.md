---
name: security-review
description: Perform a defensive application-security review of code or design, focusing on common vulnerabilities, auth, sensitive data, secrets, dependencies, trust boundaries, and secure configuration.
---

# Security Review

Perform a defensive security review of `$ARGUMENTS`.

## Check

- authentication weaknesses
- authorization / privilege escalation
- insecure direct object references
- injection risks
- unsafe deserialization
- SSRF/path traversal
- XSS/CSRF where applicable
- sensitive-data exposure
- secrets or credentials in source/config
- cryptographic misuse
- insecure transport
- logging of sensitive information
- dependency/supply-chain risk
- insecure defaults/configuration
- missing rate limits or abuse controls where relevant
- file upload/input validation
- cloud/IaC permission issues
- auditability

Do not provide instructions for exploiting real systems. Keep remediation defensive.

## Finding Format

**[Critical/High/Medium/Low] Finding**
- Evidence
- Risk
- Recommended remediation
- Verification step

End with:
### Overall Risk
### Must-Fix Before Release
### Defense-in-Depth Recommendations
