# AI SDLC Demo — Claude across the software lifecycle

Everything you need to demo how Claude can perform every stage of the SDLC using
the **Boyd AI SDLC skills** pack, plus a runnable sample project to demo against.

## What's here

```
ai-sdlc-demo/
├── README.md                 ← you are here (organize + install + customize)
├── DEMO-RUNBOOK.md           ← the live walkthrough script (start here to present)
├── AI-SDLC-Demo-Deck.pptx    ← the presentation deck for the team
├── skills-pack/              ← the original 12-skill library (portable, drop into any repo)
│   ├── README.md
│   ├── manifest.json
│   ├── .claude/skills/       ← the 12 skills
│   └── references/           ← Boyd enterprise standards (customize these)
├── sample-repo/
│   └── order-service/        ← runnable Spring Boot service, skills pre-installed
└── demo-inputs/
    ├── PROJ-142-discount-code.md   ← the feature ticket the demo carries end-to-end
    └── INC-207-incident-log.md     ← the incident used in the root-cause step
```

## The 12 skills, by SDLC stage

| Stage | Skill | Does |
|---|---|---|
| Requirements | `/analyze-requirement` | Pulls a requirement apart: facts vs assumptions, gaps, risks, impact |
| Requirements | `/create-user-story` | Turns it into a testable story with acceptance criteria & DoD |
| Design | `/generate-technical-design` | Produces an implementation design grounded in the real repo |
| Design | `/architecture-review` | Grades a design; gives an approve / needs-revision decision |
| Build | `/generate-code` | Implements a change following existing repo conventions |
| Build | `/code-review` | Reviews a diff for correctness, maintainability, tests |
| Security | `/security-review` | Defensive review: auth, injection, secrets, PII, config |
| Test | `/generate-unit-tests` | Writes focused unit tests in the project's framework |
| Test | `/generate-test-cases` | Builds a structured QA test matrix traced to acceptance criteria |
| Operate | `/root-cause-analysis` | Evidence-based incident diagnosis from logs/metrics |
| Docs | `/generate-documentation` | Generates README / runbook / Confluence-style docs from code |
| Release | `/release-readiness-review` | Go / no-go scorecard across quality, security, ops |

## How to run the demo

Open **`DEMO-RUNBOOK.md`** and follow it top to bottom. In short: launch Claude
Code from `sample-repo/order-service`, then carry ticket **PROJ-142** through all
12 skills — each skill's output feeds the next.

## Install the skills (for real use, after the demo)

**Per project** — copy the `.claude` folder into a repo root:
```bash
cp -r skills-pack/.claude /path/to/your-repo/
cp -r skills-pack/references /path/to/your-repo/.claude/references   # optional
```
Launch Claude Code from that repo and invoke a skill, e.g. `/code-review src/`.

**Personal (all your projects)** — copy the skill folders into your home skills
directory:
```bash
cp -r skills-pack/.claude/skills/* ~/.claude/skills/
```

**Shared/governed** — check `skills-pack/` into a shared engineering repo and
version it, so the whole team gets the same, reviewed skills.

## Make them Boyd-specific

The skills ship as safe, generic templates. The high-leverage customization is
the standards file:

1. Open `skills-pack/references/boyd-enterprise-standards.md`.
2. Replace every `[ADD]` placeholder with your approved standards — coding
   conventions, architecture patterns, security controls, test-coverage targets,
   API conventions, CI/CD quality gates, Jira/Confluence norms.
3. (Optional) In a skill's `SKILL.md`, add a line like:
   > Before making recommendations, inspect the applicable standards under
   > `references/` and treat them as authoritative for this repository.

Where Claude Code has approved access to Jira, Confluence, GitHub/GitLab, or
CI/CD, the skills can use that context when a ticket, PR, or page is explicitly
referenced — and they're instructed not to invent details they can't verify.

## Design principles baked into the skills

Inspect before changing · separate facts from assumptions · prefer existing repo
patterns over new frameworks · never expose secrets · never deploy/merge/delete
production resources unless explicitly authorized · always pair implementation
with validation and tests.
