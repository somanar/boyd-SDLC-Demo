# Boyd AI SDLC Skills Starter Pack

A reusable Claude Code skill library for common enterprise SDLC activities.

## Included skills

1. `/analyze-requirement`
2. `/create-user-story`
3. `/generate-technical-design`
4. `/architecture-review`
5. `/generate-code`
6. `/code-review`
7. `/security-review`
8. `/generate-unit-tests`
9. `/generate-test-cases`
10. `/root-cause-analysis`
11. `/generate-documentation`
12. `/release-readiness-review`

## Install for one project

Copy the `.claude` folder from this package into the root of your repository.

Example:

```text
your-repository/
├── .claude/
│   └── skills/
│       ├── analyze-requirement/
│       │   └── SKILL.md
│       └── ...
├── src/
└── ...
```

Then start Claude Code from the repository and invoke a skill, for example:

```text
/code-review src/
/generate-unit-tests src/services/OrderService.java
/analyze-requirement PROJ-123
```

## Install as personal skills

If you want the skills available across projects, copy the skill folders into:

```text
~/.claude/skills/
```

## Boyd customization

The skills are intentionally safe starter templates. Customize the placeholders in
`references/boyd-enterprise-standards.md` with approved:

- coding standards
- architecture patterns
- security controls
- logging/observability standards
- test coverage expectations
- API conventions
- data handling requirements
- CI/CD quality gates
- Jira/Confluence conventions

For tighter governance, check this pack into a shared engineering repository and version it.

## Connected tools

Where Claude Code has access to Jira, Confluence, GitHub/GitLab, CI/CD, or other systems
through approved integrations, the skills can use that context. The skill should not invent
information when a referenced external artifact cannot be accessed.

## Design principles

- Inspect before changing.
- Distinguish facts from assumptions.
- Prefer existing repository patterns over introducing new frameworks.
- Do not expose credentials or secrets.
- Do not deploy, merge, delete, or alter production resources unless explicitly authorized.
- Include validation/testing steps with implementation work.
