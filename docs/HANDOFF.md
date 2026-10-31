# HANDOFF — living status

Read this right after CLAUDE.md. Update at the end of **every** session. Newest log entry on top.

## Current state
- **Date of last update:** 2026-10-31
- **Phase:** 0 (Foundations) — in progress
- **Repo:** github.com/YatharthJorwal/Ampere (first commit `0e66f28`: docs only; CI#1 green)
- **Build status:** UNKNOWN — Gradle project is bootstrapped by the owner from Android Studio; bundle 01 sources have never been compiled
- **Last release tag:** none

## What exists
- Docs and workflows (committed).
- Bundle 01 (written, untested): `MainActivity`, skin tokens (`ui/theme/SkinTokens.kt`), default skin `ClassicLcdSkin` (code constant; moves to a skin pack in P3-04), `LocalSkin`, `AmpereTheme`, `LcdPanel`, `MiniPlayerPlaceholder`, placeholder screens, `AmpereApp` nav shell (4 destinations), `ampere_strings.xml`, `docs/ASSETS.md`, ADR-0016/17/18, CI/release workflows trimmed to tasks that exist.

## Next up (in order)
1. Owner: create project in Android Studio, copy into repo, apply bundle 01, build, push. Paste back: build result (or errors), `settings.gradle.kts`, `gradle/libs.versions.toml`, `app/build.gradle.kts`.
2. P0-02 finish: add `:dsp` module (Android Studio "Kotlin library" wizard) + first pure-Kotlin code/tests (EQ band constants, dB helpers). CI then also runs `:dsp:test`.
3. P0-04: Spotless + ktlint + detekt, then re-enable them in `ci.yml`/`release.yml`.
4. P0-08: signing config in `app/build.gradle.kts`, keystore, secrets, tag `v0.0.1`.
5. P0-09: About screen licenses list.
Exit of Phase 0 is an approval gate.

## In progress / half-finished
- P0-02, P0-03, P0-05, P0-06 marked `[~]`: code written, awaiting first successful build and an on-device look.
- P0-03 reduced by ADR-0016 (no Hilt in Phase 0).

## Open questions for the owner
1. Final app name (repo is `Ampere`; ADR-0015). Application id in use: `site.yatharthwebs.ampere`.
2. Project license (ADR-0014).
3. Confirm `minSdk 26` (ADR-0011).
4. GitHub Releases only, or Play Store later too?
5. Release keystore creation and backup (docs/RELEASE.md section 3).

## Known risks
See docs/PHASES.md Risks R1–R8. New: first-build compile errors in bundle 01 are possible since it was written without a compiler.

## Environment notes
- IDE: Android Studio. Windows PowerShell for git. Gradle wrapper on Windows: `.\gradlew.bat`.
- AI sessions cannot run Gradle/emulator here; code is marked untested until the owner confirms a build.
- Clean-room: nobody involved has read Winamp source code.

## Session log
### 2026-10-31 (2) — Bundle 01
- Wrote Phase 0 app sources and doc updates; trimmed CI to existing Gradle tasks (spotless/detekt/:dsp:test return with P0-04 and `:dsp`).
- Nothing compiled. Awaiting owner build output.
### 2026-10-31 (1) — Planning session
- Researched feature set, chose stack, wrote all planning docs and workflows. ADR-0001…0015.
