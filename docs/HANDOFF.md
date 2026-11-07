# HANDOFF — living status

Read this right after CLAUDE.md. Update at the end of **every** session. Newest log entry on top.

## Current state
- **Date of last update:** 2026-11-01
- **Phase:** 1 (Player core) started. Phase 0 exit: the owner installed the app on a real phone and it works (v0.0.1 release workflow result not explicitly confirmed; the owner fixed the missing-secret failure).
- **Repo:** github.com/YatharthJorwal/Ampere, local copy at `E:\ampere-project\ampere`
- **Build/CI:** last confirmed green: CI #3 (e6c0c5a). Bundle 04 not yet built by the owner.
- **Last release tag:** `v0.0.1`

## What exists
- Phase 0 complete (shell, skins tokens, `:dsp`, Spotless, signing, CI, release workflow).
- Bundle 04 (written, awaiting owner build): nav label fix ("Now playing" single line), `AmpereApplication` + `AppContainer`, audio permission flow with rationale/open-settings screen (P1-01), `MediaStoreTrackSource` reading MediaStore directly (P1-02, no Room yet), `LibraryViewModel`, Songs list with instant search, rescan, empty/loading/error states (P1-06 partial: Songs tab only), `TrackRow`, repo-owned `AndroidManifest.xml`.
- Verified in sandbox: pure-Kotlin domain code (`Track`, `TrackSearch`, `DurationFormat`) compiles and its 7 tests pass; ktlint passes on everything. NOT verified: Android code (needs a real Gradle build), the manifest (rewritten from the Studio template, assuming theme name `Theme.Ampere`).

## Next up (in order)
1. Owner: apply bundle 04, `.\gradlew.bat spotlessCheck testDebugUnitTest assembleDebug`, run on the phone, check permission prompt, song list, search, rescan. Report errors or screenshots.
2. P1-04/P1-05: Media3 (1.11.1) `PlaybackService` + `MediaSession` + foreground notification, `PlaybackRepository`/`PlayerState`, tap a song to play, mini player and Now playing transport (play/pause/seek/next/prev).
3. P1-06 rest: Albums/Artists/Genres/Folders tabs (needs a navigation approach for detail screens, ADR-0018 revisit), P1-07 album art.
4. P1-08/09/10 (queue, shuffle/repeat, resume, playlists + M3U) with Room + KSP in their own bundle (ADR-0022).
5. P1-03 tag reader decision, P1-12 settings basics, P1-13 test pass, release `v0.1.0`.

## In progress / half-finished
- P1-01, P1-02, P1-06 are `[~]`.

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
### 2026-11-01 — Bundle 04 (Phase 1 slice 1)
- Researched versions (Media3 1.11.1, Room 2.8.5, KSP 2.3.13). Chose manual DI (ADR-0021) and staged Room (ADR-0022). Wrote permission flow, MediaStore source, library screen, nav label fix.
### 2026-11-01 — Bundle 03
- CI green. Wrote release signing (env-var based), version properties, licenses text. Nothing built yet by owner.
### 2026-11-01 — Bundle 02 patch a
- First owner build: `spotlessCheck` failed on CRLF vs LF line endings (Windows). Fixed by enforcing LF; also `@param:StringRes` to silence a Kotlin 2.2 annotation-target warning.
### 2026-11-01 — Bundle 02
- Added `:dsp` module with first code/tests, Spotless + ktlint, `.editorconfig`, CI on JDK 25. Deferred detekt (ADR-0019). Reformatted bundle 01 sources with ktlint and moved the nav dot into its own composable.
### 2026-10-31 (2) — Bundle 01
- App shell written; owner bootstrapped the project from Android Studio, moved dev files to E:, and confirmed it runs on the emulator.
### 2026-10-31 (1) — Planning session
- Researched feature set, chose stack, wrote all planning docs and workflows. ADR-0001…0015.
