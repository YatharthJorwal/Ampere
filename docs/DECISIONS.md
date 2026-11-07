# DECISIONS (ADR log)

Append-only. New decision = new entry at the bottom. To change a decision, add a new entry that supersedes the old one (set old status to `Superseded by ADR-xxxx`).
Format: Status · Date · Context · Decision · Consequences.

---
## ADR-0001 — Clean-room development
Status: Accepted · 2026-10-31
Context: Winamp's desktop source is source-available with restrictions on redistributing modified versions; name and art are trademarks; the official Android app is closed source.
Decision: Build from scratch using only idea-level features, public formats/protocols, and independently licensed references. See CLEAN_ROOM.md.
Consequences: Own name, icon, skins. Slightly more design work; no legal exposure.

## ADR-0002 — Native Android with Kotlin
Status: Accepted · 2026-10-31
Context: Needs low-latency custom DSP, background service, visualizers, deep skinning. Owner's background is web/React.
Decision: Native Kotlin. Not React Native, not Flutter.
Consequences: Best access to Android audio/media APIs. iOS would be a separate effort (Kotlin Multiplatform is a later option).

## ADR-0003 — Jetpack Compose for UI
Status: Accepted · 2026-10-31
Decision: Compose (Material 3 components only where they fit; most visuals custom via skin tokens). No XML layouts.
Consequences: Declarative model similar to React; previews speed iteration; skinning through CompositionLocal.

## ADR-0004 — Media3 (ExoPlayer) + MediaSessionService
Status: Accepted · 2026-10-31
Decision: Playback via Media3 ExoPlayer inside a `MediaSessionService`; UI uses `MediaController`.
Consequences: Gets notification, lock screen, Bluetooth, Android Auto, gapless (native) with little code. Crossfade needs custom work (R1).

## ADR-0005 — Custom 10-band EQ in an AudioProcessor
Status: Accepted · 2026-10-31
Context: `android.media.audiofx.Equalizer` varies per device and often exposes few bands.
Decision: Implement RBJ-cookbook peaking biquads in `:dsp`, run inside a Media3 `AudioProcessor`.
Consequences: Consistent sound and exact Winamp-like band centers; we own correctness/performance. Must handle encodings and offload (R5).

## ADR-0006 — Visualizer data from our own PCM tap
Status: Accepted · 2026-10-31
Context: `android.media.audiofx.Visualizer` needs `RECORD_AUDIO` and is device-dependent.
Decision: Tap post-EQ PCM in an `AudioProcessor`, FFT on a separate thread.
Consequences: No microphone-style permission; analyzer reflects EQ; we own the FFT code.

## ADR-0007 — Room over MediaStore for the library
Status: Accepted · 2026-10-31
Decision: MediaStore is the file index; Room is our metadata/rating/playlist store, diffed incrementally.
Consequences: Fast queries and custom fields; need a sync layer and migrations.

## ADR-0008 — Hilt for DI
Status: Accepted · 2026-10-31
Decision: Hilt with KSP.
Consequences: Standard Android integration for service/ViewModels. Adds build-time cost.

## ADR-0009 — Skins and visualizer presets are data, not code
Status: Accepted · 2026-10-31
Context: Winamp's plugin model (loading arbitrary code) doesn't fit Android's security model.
Decision: `.ampskin` zip with JSON + assets; visualizer presets as JSON + GLSL text. No dynamic code loading.
Consequences: Safe imports; less flexible than code plugins.

## ADR-0010 — Release via GitHub Actions on version tags
Status: Accepted · 2026-10-31
Decision: Pushing tag `vX.Y.Z` runs `release.yml`: builds signed APK + AAB, attaches to a GitHub Release with SHA256SUMS. `ci.yml` runs checks on PRs/pushes.
Consequences: Distribution by APK sideload/GitHub first; store publishing later is optional. Signing key custody matters (RULES §8).

## ADR-0011 — SDK levels
Status: Proposed · 2026-10-31
Decision (proposed; used at bootstrap): `minSdk 26` (Android 8.0). Bootstrap template set `compileSdk` 37 and `targetSdk` 37.
Consequences: Covers the large majority of active devices, simplifies notification channels and java.time use. Revisit if user feedback needs older devices.

## ADR-0012 — Two modules to start
Status: Accepted · 2026-10-31
Decision: `:app` + pure-Kotlin `:dsp`. Split more only when justified.
Consequences: Simple build; DSP tests run fast on JVM and can't accidentally depend on Android.

## ADR-0013 — Trunk-based git with Conventional Commits
Status: Accepted · 2026-10-31
Decision: Short-lived branches, squash merges, Conventional Commits, SemVer tags.

## ADR-0014 — Project license
Status: Open · 2026-10-31
Context: Not yet chosen. Do not adopt a Winamp-style restrictive license.
Options: MIT / Apache-2.0 (permissive), GPL-3.0 (copyleft), proprietary (source private).
Decision: Pending owner. Until decided, no LICENSE file; repo is "all rights reserved".

## ADR-0015 — Project name and application id
Status: Open · 2026-10-31
Context: "Ampere" is a working codename; application id proposed as `site.yatharthwebs.ampere`.
Update 2026-10-31: GitHub repo created as `YatharthJorwal/Ampere`; bootstrap uses package `site.yatharthwebs.ampere`.
Decision: Final name pending owner. Do a name/trademark/store-name check before public release. Changing the application id after any public release breaks in-place updates — settle before `v0.1.0`.

---
## ADR-0016 — Introduce Hilt, Room and Media3 when first needed (not in Phase 0)
Status: Accepted · 2026-10-31
Context: Library versions must match the AGP/Kotlin/KSP set that the Android Studio template generates. Adding unused dependencies in Phase 0 adds build risk with no benefit.
Decision: Phase 0 adds no DI, database, or media dependencies. Hilt arrives with the first service/ViewModel that needs it (P1-04/P1-05), Room with the scanner (P1-02), Media3 with the playback service (P1-04). Versions are looked up and recorded here at that time. ADR-0008 (choice of Hilt) stands; only the timing changes. P0-03 is reduced to package/app-id/Application setup.
Consequences: Smaller first build; each dependency lands with the code that proves it works.

## ADR-0017 — Bootstrap from the Android Studio template
Status: Accepted · 2026-10-31
Context: AI sessions cannot run Gradle here, so hand-written build files would be untested guesses at plugin versions.
Decision: The Gradle project (AGP, Kotlin, Compose, wrapper) comes from the Android Studio "Empty Activity" template (Kotlin DSL, minSdk 26, package `site.yatharthwebs.ampere`). Later build changes (`:dsp` module, spotless/detekt, signing) are written against the real generated `libs.versions.toml`, `settings.gradle.kts` and `app/build.gradle.kts`.
Consequences: Known-good toolchain. First build output must be checked by the owner.

## ADR-0018 — State-based navigation shell until detail screens exist
Status: Accepted · 2026-10-31
Decision: Phase 0 top-level navigation is a `rememberSaveable` enum `Destination` in `AmpereApp`. Adopt a navigation library (ADR with version) when the first detail screen needs a back stack (album/artist detail, P1-06).
Consequences: No extra dependency now; small refactor later.

---
## ADR-0019 — Defer detekt; keep Spotless + ktlint
Status: Accepted · 2026-11-01
Context: The newest stable detekt tag found is v1.23.8; everything newer is a 2.0.0 alpha. It has not been verified against Kotlin 2.2 / AGP 9.5 here, and a failing static-analysis plugin would block the build for no product benefit.
Decision: Phase 0 enforces style with Spotless + ktlint 1.8.0 (`.editorconfig` at the repo root). detekt is revisited when a stable release with Kotlin 2.2+ / AGP 9 support exists; then add it with its own ADR.
Consequences: Formatting and basic lint rules are enforced; deeper static analysis is postponed. P0-04 is considered done when `spotlessCheck` passes in CI.

## ADR-0020 — Toolchain facts and CI JDK
Status: Accepted · 2026-11-01
Context: The template generated `gradle/gradle-daemon-jvm.properties` with `toolchainVersion=25`; the foojay resolver is enabled in `settings.gradle.kts`.
Decision: CI and release workflows install Temurin JDK 25 so the Gradle daemon requirement is met deterministically. Bytecode targets stay Java 11 (`app` and `:dsp`). Recorded versions at bootstrap: Gradle 9.6.0 (wrapper), AGP 9.5.0, Kotlin 2.2.10, Compose BOM 2026.02.01, Spotless plugin 8.10.4, ktlint 1.8.0.
Consequences: Bumping any of these needs a DECISIONS entry. `:dsp` applies `org.jetbrains.kotlin.jvm` at the same version as the catalog `kotlin` entry.

---
## ADR-0021 — Manual DI instead of Hilt for now
Status: Accepted · 2026-11-01
Context: Hilt needs a Gradle plugin plus KSP. Compatibility of that combination with AGP 9.5's built-in Kotlin and Kotlin 2.2.10 was not verified, and a broken plugin stack would block every other task. Dagger's newest stable tag found is 2.60.1.
Decision: Phase 1 uses hand-wired dependencies: `AppContainer` (created by `AmpereApplication`) and `ViewModelProvider.Factory` classes. Supersedes the timing and, for now, the choice in ADR-0008 and ADR-0016.
Consequences: No DI plugin. More boilerplate per screen. Migration to Hilt later is mechanical (constructors already take their dependencies).

## ADR-0022 — Staged persistence and dependency versions for Phase 1
Status: Accepted · 2026-11-01
Context: Room needs KSP (same risk class as Hilt) and is only essential once playlists and queue persistence exist.
Decision: P1-02/P1-06 read MediaStore directly (no database). Room, KSP and the schema come with playlists and queue persistence (P1-09/P1-10) in their own bundle so a toolchain problem is isolated. Versions chosen from the official release pages and tags (checked 2026-11-01): Media3 1.11.1, Room 2.8.5 (stable; Room 3 `androidx.room3` is still alpha, not used), KSP 2.3.13 (independent of Kotlin; works with Kotlin 2.2 and newer). Avoid `@Parcelize` on Room entities (open KSP issue with AGP 9 built-in Kotlin).
Consequences: Library list is not cached between launches yet (a MediaStore query is fast). Ratings and play counts (P2) will need Room first.

## ADR-0023 — The repo owns AndroidManifest.xml
Status: Accepted · 2026-11-01
Decision: `app/src/main/AndroidManifest.xml` is maintained in the repo from bundle 04 onward (declares `AmpereApplication`, audio-read permissions; the playback service and foreground-service permissions arrive in P1-04).
