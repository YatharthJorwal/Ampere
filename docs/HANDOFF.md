# HANDOFF — living status

Read this right after CLAUDE.md. Update at the end of **every** session. Newest log entry on top.

## Current state
- **Date of last update:** 2026-11-01
- **Phase:** 0 (Foundations) — in progress
- **Repo:** github.com/YatharthJorwal/Ampere, local copy at `E:\ampere-project\ampere` (C: was out of space)
- **Build status:** bundle 01 (app shell) builds and runs on the Android Studio emulator (Medium Phone, API 37): LCD panel, bottom nav, mini-player. Confirmed by owner screenshot. Bundle 02 (`:dsp`, Spotless) has NOT been built on the owner's machine yet.
- **Last release tag:** none

## What exists
- Docs and workflows.
- App shell: skin tokens, default skin `ClassicLcdSkin` (code constant until P3-04), `AmpereTheme`, `LcdPanel`, `MiniPlayerPlaceholder`, state-based nav (4 destinations), placeholder screens.
- Bundle 02: `:dsp` module (pure Kotlin) with `Decibels` and `EqBands` + tests; Spotless/ktlint config (`.editorconfig`, root `build.gradle.kts`); CI uses JDK 25 and runs `spotlessCheck`, `lintDebug`, unit tests incl. `:dsp:test`, `assembleDebug`.
- Verified in the AI sandbox: `:dsp` compiles with Kotlin 2.2.10 and its 9 tests pass (run against a minimal JUnit stand-in); ktlint 1.8.0 passes on all Kotlin and Gradle scripts. NOT verified: the Gradle wiring itself (kotlin-jvm plugin next to AGP 9.5, Spotless plugin), which can only be checked by a real Gradle run.

## Next up (in order)
1. Owner: apply bundle 02, run `.\gradlew.bat spotlessCheck :dsp:test assembleDebug`, commit, push, and report the CI result.
2. P0-08: release signing in `app/build.gradle.kts` (AGP 9 uses the new `optimization {}` DSL, so write against the real file), create keystore, GitHub secrets, tag `v0.0.1`.
3. P0-09: About screen licenses list.
4. Phase 0 exit review, then Phase 1 (P1-01 permissions). Phase 1 adds Room, Media3 and Hilt; look up their versions then (ADR-0016).

## In progress / half-finished
- P0-02 and P0-04 are `[~]` until bundle 02 builds in CI.
- P0-08, P0-09 not started.

## Open questions for the owner
1. Final app name (repo is `Ampere`; ADR-0015). Application id in use: `site.yatharthwebs.ampere`.
2. Project license (ADR-0014).
3. Confirm `minSdk 26` (ADR-0011).
4. GitHub Releases only, or Play Store later too?
5. Release keystore creation and backup (docs/RELEASE.md section 3).

## Known risks
See docs/PHASES.md Risks R1–R8. Possible Gradle plugin version clash when `:dsp` applies `kotlin-jvm` alongside AGP 9.5's built-in Kotlin; fix path is to align versions in `libs.versions.toml`.

## Environment notes
- IDE: Android Studio. Windows PowerShell for git. Wrapper: `.\gradlew.bat`.
- Disk: Android SDK, AVDs and the Gradle cache live on `E:\Android\` (env vars ANDROID_HOME, ANDROID_AVD_HOME, GRADLE_USER_HOME). `local.properties` points sdk.dir at `E:\Android\Sdk`.
- AI sessions cannot run Gradle or the emulator here; they can compile pure-Kotlin code with kotlinc and run ktlint. Anything else is marked untested until the owner confirms a build.
- Line endings: LF enforced via `.gitattributes`, `.editorconfig` and Spotless. If `spotlessCheck` fails on a fresh Windows checkout, run `.\gradlew.bat spotlessApply` once.
- Clean-room: nobody involved has read Winamp source code.

## Session log
### 2026-11-01 — Bundle 02 patch a
- First owner build: `spotlessCheck` failed on CRLF vs LF line endings (Windows). Fixed by enforcing LF; also `@param:StringRes` to silence a Kotlin 2.2 annotation-target warning.
### 2026-11-01 — Bundle 02
- Added `:dsp` module with first code/tests, Spotless + ktlint, `.editorconfig`, CI on JDK 25. Deferred detekt (ADR-0019). Reformatted bundle 01 sources with ktlint and moved the nav dot into its own composable.
### 2026-10-31 (2) — Bundle 01
- App shell written; owner bootstrapped the project from Android Studio, moved dev files to E:, and confirmed it runs on the emulator.
### 2026-10-31 (1) — Planning session
- Researched feature set, chose stack, wrote all planning docs and workflows. ADR-0001…0015.
