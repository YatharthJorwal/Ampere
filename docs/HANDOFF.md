# HANDOFF — living status

Read this right after CLAUDE.md. Update at the end of **every** session. Newest log entry on top.

## Current state
- **Date of last update:** 2026-10-31
- **Phase:** 0 (Foundations) — not started
- **Branch/commit:** n/a (repo not yet created)
- **Build status:** no Gradle project yet
- **Last release tag:** none

## What exists
- Planning docs only: CLAUDE.md, README.md, docs/{SPEC, ARCHITECTURE, UI_SPEC, PHASES, RULES, DECISIONS, CLEAN_ROOM, RELEASE, HANDOFF}.md, `.github/workflows/{ci,release}.yml`, `.gitignore`.

## Next up (in order)
1. P0-01: Create GitHub repo; commit these files as the first commit (`docs: add project planning docs`).
2. P0-02: Generate Gradle project in Android Studio (Empty Activity / Compose), add `:dsp` module, set up `libs.versions.toml`.
3. P0-03 … P0-09 per PHASES.md.

## In progress / half-finished
Nothing.

## Open questions for the owner
1. Final app name and application id (ADR-0015). Proposed id: `site.yatharthwebs.ampere`.
2. Project license (ADR-0014).
3. Confirm `minSdk 26` (ADR-0011).
4. Publish only on GitHub Releases for now, or also plan for Play Store later?
5. Who creates the release keystore and how is the backup kept? (RELEASE.md §3)

## Known risks to keep in mind
See PHASES.md → Risks (R1–R8). Highest: crossfade (R1), processor bypass/offload (R5).

## Environment notes
- Android Studio is the IDE; emulator via Device Manager; prefer a real device for audio testing.
- AI sessions that cannot run Gradle/emulator (e.g. chat without a build environment) must say so and mark code as **untested** in this file.
- Clean-room: nobody involved so far has read Winamp source code (update this line if that changes).

## Session log
### 2026-10-31 — Planning session
- Researched feature set, chose stack (Kotlin, Compose, Media3, custom DSP EQ), IDE (Android Studio).
- Wrote all planning docs and CI/release workflows.
- No code written. Decisions recorded as ADR-0001…0015 (0011, 0014, 0015 not final).
- Next session should start at P0-01.
