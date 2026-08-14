# Copilot Instructions

## Guardrails for AI
When executing AI instructions:
- NEVER stage modifies lines on git, let the user review the modification and decide what to keep;
- NEVER unstage staged lines on git, but feel free to modify staged files;
- NEVER commit on the repository;
- NEVER push on the repository;
- NEVER change the repository history on remote branches, unless working with un-pushed branches AND ONLY when asked explicitly.

## Instructions
Please read and adhere to our codebase configurations found in the root directory:
- See `.github/ai/identity.md` for your required persona and communication rules.
- See `.github/ai/goal.md` for our current product goals and roadmap boundaries.
- See `.github/ai/system.md` for our explicit tech stack guidelines.
