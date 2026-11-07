# PHASES

Roadmap with task IDs. Tick boxes as tasks finish. Each phase ends at an **approval gate**: stop, summarize, wait for the owner.
Task ID format: `P<phase>-<nn>`. Use it in commit messages. F-IDs refer to SPEC.md.

Legend: [ ] todo · [~] in progress · [x] done

## Phase 0 — Foundations
Goal: an empty-but-real app that builds in CI and publishes a GitHub Release.
- [x] P0-01 Create GitHub repo, add these docs, branch protection on `main` (optional), set repo secrets later
- [x] P0-02 Gradle project: `:app` (Compose, Hilt, Room, Media3 deps via `gradle/libs.versions.toml`) + `:dsp` (Kotlin JVM). Record chosen SDK levels and library versions in DECISIONS.md
- [x] P0-03 Application id/package decided (see HANDOFF open questions), app icon placeholder (original), `Application` class, Hilt wired
- [x] P0-04 Spotless + ktlint + detekt configured; `./gradlew spotlessCheck detekt` passes
- [x] P0-05 Theme layer: `SkinTokens`, `LocalSkin`, default "Classic LCD" skin as code constants; previews render
- [x] P0-06 Navigation shell: bottom bar with placeholder screens, mini-player placeholder
- [x] P0-07 CI workflow (`ci.yml`) green on PR/push
- [x] P0-08 Release workflow (`release.yml`) + signing config; create keystore; add secrets; tag `v0.0.1` produces a GitHub Release with signed APK + AAB + SHA256SUMS
- [x] P0-09 `docs/ASSETS.md` created (license log), About screen stub with licenses (use a licenses plugin or manual list)
**Exit:** tag → signed APK downloadable from GitHub Releases and installs on a phone; CI green.

## Phase 1 — Player core (MVP)
Goal: a solid, boring-in-a-good-way music player.
- [~] P1-01 Permissions flow (READ_MEDIA_AUDIO, notifications) with rationale screens
- [~] P1-02 MediaStore scanner → Room (tracks/artists/albums), incremental rescan, progress state (F-LIB-01)
- [ ] P1-03 Tag reader choice (license-checked) + ReplayGain/lyrics tag plumbing stubs; ADR
- [ ] P1-04 `PlaybackService` + ExoPlayer + MediaSession + foreground notification (F-PLAY-01/02/11, F-SYS-01)
- [ ] P1-05 `PlaybackRepository` + `PlayerState` flow; mini player wired
- [~] P1-06 Library screens: Songs/Albums/Artists/Genres/Folders, fast scroll, search (F-LIB-02/03)
- [ ] P1-07 Album art loading + cache (F-LIB-04)
- [ ] P1-08 Queue: reorder, remove, play next/add (F-LIST-01)
- [ ] P1-09 Shuffle/repeat + persistence; queue/position resume across restarts (F-PLAY-04/05)
- [ ] P1-10 Saved playlists + M3U/M3U8 import/export (F-LIST-02/03)
- [ ] P1-11 Now playing screen: LCD panel (time, title, tech line), seek bar, transport row (no analyzer yet)
- [ ] P1-12 Settings basics (scan folders, rescan, about) (F-SYS-05)
- [ ] P1-13 Format test pass (MP3/AAC/FLAC/OGG/Opus/WAV fixtures), headset/Bluetooth button test, background soak 1 hour
**Exit:** daily-drivable player; all P1 acceptance criteria met; release `v0.1.0`.

## Phase 2 — Signature features
Goal: the things that make it feel like *this* player.
- [ ] P2-01 `:dsp` biquad + RBJ peaking filter + tests (frequency response)
- [ ] P2-02 `EqProcessor` in ExoPlayer chain; 16-bit and float handling; smoothing; limiter (F-EQ-01/02) — spike R5 result recorded
- [ ] P2-03 EQ UI sheet, presets (built-in + user), on/off, balance (F-EQ-03/04/05)
- [ ] P2-04 `:dsp` FFT, window, ring buffer + tests
- [ ] P2-05 `AnalysisTapProcessor` + analysis thread + `SpectrumFrame` flow
- [ ] P2-06 Analyzer + oscilloscope components on Now playing (F-VIZ-01/02)
- [ ] P2-07 Gapless verification + fixes (F-PLAY-06)
- [ ] P2-08 Spike: crossfade approaches → ADR → implement (F-PLAY-07) (Risk R1)
- [ ] P2-09 ReplayGain apply (F-PLAY-08), playback speed (F-PLAY-09)
- [ ] P2-10 Sleep timer with fade (F-PLAY-10)
- [ ] P2-11 Ratings, play counts, smart playlists (F-LIB-05, F-LIST-05), PLS + sort/dedupe (F-LIST-04/06)
- [ ] P2-12 Tag viewer + lyrics (embedded + .lrc) (F-LIB-06/08)
- [ ] P2-13 Open-with/share intent (F-SYS-04)
- [ ] P2-14 Audio soak: 2 hours with EQ + analyzer, no glitches, battery check
**Exit:** all P2 criteria met; release `v0.2.0`.

## Phase 3 — Skins and visualizers
- [ ] P3-01 Skin parser/validator (`skin/`) with full malicious-input test set (F-SKIN-03)
- [ ] P3-02 Skin registry, live switching, persistence (F-SKIN-01)
- [ ] P3-03 Refactor all components to read only from `LocalSkin`; per-skin previews
- [ ] P3-04 Four bundled skins + licenses logged (F-SKIN-02)
- [ ] P3-05 Skin picker screen + `.ampskin` import via file picker
- [ ] P3-06 Dynamic color skin (F-SKIN-05)
- [ ] P3-07 Sprite mode for transport/slider (UI_SPEC §6.3)
- [ ] P3-08 Fullscreen GL visualizer host + 3 bundled shader presets (F-VIZ-03/04)
- [ ] P3-09 Tag editor with scoped-storage write consent (F-LIB-07) (Risk R3)
- [ ] P3-10 EQ response-curve display
**Exit:** skins swap live without restart; import survives hostile packs; release `v0.3.0`.

## Phase 4 — Connected and polish
- [ ] P4-01 Radio playback + ICY metadata + reconnect (F-NET-01/04)
- [ ] P4-02 Radio Browser directory + favorites (F-NET-02)
- [ ] P4-03 Podcasts: search, subscribe, episodes, resume (F-NET-03)
- [ ] P4-04 Home-screen widget (F-SYS-02)
- [ ] P4-05 Android Auto (F-SYS-03), test on Desktop Head Unit
- [ ] P4-06 Backup/export/import (F-SYS-06)
- [ ] P4-07 Stretch: `.wsz` importer spike (F-SKIN-04) (Risk R4)
- [ ] P4-08 Stretch: Milkdrop-style engine evaluation (F-VIZ-05), license review
**Exit:** release `v0.4.0`.

## Phase 5 — Hardening and public release
- [ ] P5-01 Accessibility pass (TalkBack, font scale, contrast)
- [ ] P5-02 Performance pass (startup, scroll, analyzer CPU, memory); baseline profile
- [ ] P5-03 Crash/ANR review on several devices; Android version matrix test
- [ ] P5-04 Name, icon, trademark check; CLEAN_ROOM release checklist
- [ ] P5-05 Privacy policy + store listing text (if publishing to a store)
- [ ] P5-06 `v1.0.0`

## Risks and unknowns
| ID | Risk | Plan |
|---|---|---|
| R1 | Crossfade has no built-in ExoPlayer support | Spike P2-08: dual-player overlap vs custom mixing; ship after gapless is solid |
| R2 | Aggressive OEM battery killers stop playback | Foreground service + test on several brands; in-app help page |
| R3 | Tag editing on scoped storage (Android 11+) needs user write consent per file/batch | Use `MediaStore.createWriteRequest`; keep tag editor behind P3; fallback read-only |
| R4 | `.wsz` format mapping is fiddly and skins are copyrighted | Import-only, best effort, stretch goal |
| R5 | ExoPlayer may deliver 16-bit or float PCM depending on settings/device; some outputs bypass processors (offload/passthrough) | Verify in P2-02; disable offload when EQ on; document |
| R6 | Analyzer jank on low-end devices | Reduce bar count/FFT size adaptively; skip frames |
| R7 | Cleartext HTTP radio streams vs network security policy | Narrow config + ADR in P4 |
| R8 | Library/scan performance on 50k+ tracks | Paged queries, indexed columns, benchmark in P1-02 |
