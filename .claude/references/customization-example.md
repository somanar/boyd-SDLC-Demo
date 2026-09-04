# How to Make These Boyd-Specific

You can enrich individual skills by instructing them to inspect approved standards
stored in your engineering repository, for example:

```text
engineering-standards/
├── architecture.md
├── java-coding-standards.md
├── api-standards.md
├── security-baseline.md
├── testing-standards.md
└── cicd-quality-gates.md
```

Then add a line to the relevant SKILL.md:

> Before making recommendations, inspect the applicable standards under
> `engineering-standards/` and treat them as authoritative for this repository.

You can also connect approved Jira/Confluence/GitHub integrations and instruct a skill
to use those sources when a ticket, page, pull request, or repository is explicitly referenced.
