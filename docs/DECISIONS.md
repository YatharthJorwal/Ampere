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
Decision (proposed; used at bootstrap): `minSdk 26` (Android 8.0). `compileSdk`/`targetSdk` = latest stable at P0-02; record exact numbers here when set.
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
