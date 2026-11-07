# ARCHITECTURE

Status: v0.1 (2026-10-31). Changes to anything here require a DECISIONS.md entry.

## 1. Overview
Native Android, Kotlin, Jetpack Compose UI, Media3 playback in a `MediaSessionService`. Two Gradle modules at start:

- `:app` — Android application (UI, service, data, DI).
- `:dsp` — pure Kotlin/JVM library: biquad filters, EQ, FFT, window functions, ring buffer. No `android.*`. Fast JVM unit tests.

Split `:app` further only when build time or boundaries demand it (ADR-0012).

```
┌────────────────────────── UI (Compose) ───────────────────────────┐
│ Screens + ViewModels (UDF: state down, events up) + Skin theme     │
└───────────────▲───────────────────────────────▲───────────────────┘
                │ StateFlow                     │ StateFlow
┌───────────────┴──────────┐        ┌───────────┴───────────────────┐
│ Domain/Repositories      │        │ PlaybackRepository             │
│ Library, Playlist, Eq,   │        │ (wraps MediaController)        │
│ Skin, Settings, Radio    │        └───────────▲───────────────────┘
└───────────────▲──────────┘                    │ Media3 session IPC
                │                    ┌──────────┴───────────────────┐
┌───────────────┴──────────┐         │ PlaybackService              │
│ Data: Room, MediaStore,  │         │ (MediaSessionService)        │
│ DataStore, Network       │         │  └ ExoPlayer                 │
└──────────────────────────┘         │     └ AudioProcessors:       │
                                     │        EqProcessor (:dsp)    │
                                     │        AnalysisTap  → ring   │
                                     └──────────┬───────────────────┘
                                                │ SpectrumFrame (Flow)
                                                ▼
                                       VisualizerRepository → UI
```

## 2. Package layout (`:app`, base `site.yatharthwebs.ampere`)
```
ui/            theme (skin tokens), components, navigation, screens/{player,library,queue,eq,radio,settings,skins}
playback/      PlaybackService, PlaybackRepository, audio/{EqProcessor,AnalysisTapProcessor}, notification, session
data/          db (Room: entities, DAOs), mediastore (scanner), prefs (DataStore), net (radio/podcast APIs), playlist (m3u/pls)
domain/        models, repository interfaces, small use cases (only where logic is non-trivial)
skin/          SkinPack model, parser, validator, registry, bundled skins (assets)
di/            Hilt modules
util/          small helpers
```
Rule: `ui` depends on `domain` and `playback` interfaces; `data` implements `domain`. `ui` never imports `data` internals. `playback` never imports `ui`.

## 3. Playback
- `PlaybackService : MediaSessionService` owns the single `ExoPlayer` and the `MediaSession`. Foreground service type `mediaPlayback`.
- UI connects through a `MediaController` wrapped by `PlaybackRepository`, which exposes `StateFlow<PlayerState>` (current item, position, isPlaying, shuffle, repeat, queue) and suspend commands. No Compose code touches `Player` directly.
- Queue persistence: on change (debounced) write queue + index + position to Room; restore on service start (F-PLAY-05).
- Audio focus and becoming-noisy handling via ExoPlayer `setAudioAttributes(..., handleAudioFocus = true)` and `setHandleAudioBecomingNoisy(true)`.
- Gapless: use ExoPlayer's native gapless support; verify with test album (F-PLAY-06).
- Crossfade: ExoPlayer has no built-in crossfade. Spike options: (a) two players with overlapping volume ramps coordinated by a controller; (b) custom audio mixing. Decide in ADR after spike (Risk R1). Not in P1.
- Playback speed: `PlaybackParameters` with pitch preserved.
- Play count: increment once per play, when position passes 50% of the duration or 4 minutes, whichever comes first.
- Radio (P4): `MediaItem` with stream URI; ICY metadata via Media3 metadata callbacks; reconnect with backoff.

## 4. Audio pipeline (DSP)
ExoPlayer is built with a custom `AudioSink` configuration providing an ordered `AudioProcessor` chain:

1. `EqProcessor` — preamp/ReplayGain gain → 10 cascaded peaking biquads (RBJ cookbook) per channel → soft limiter. Params come from an immutable `EqParams` snapshot swapped atomically (volatile ref); coefficients are recomputed off the audio thread and applied at buffer boundary with short smoothing ramp to avoid zipper noise.
2. `AnalysisTapProcessor` — copies a mono mix of the post-EQ signal into a lock-free single-producer ring buffer. Passes audio through unchanged.

Formats: handle `ENCODING_PCM_16BIT` and `ENCODING_PCM_FLOAT`; for other encodings pass through (EQ disabled for that stream) and log once. Prefer float internally. Document what ExoPlayer actually delivers during P2 spike (R5).

Bands (Hz): 60, 170, 310, 600, 1000, 3000, 6000, 12000, 14000, 16000. Gain range ±12 dB. Fixed Q chosen in P2 and recorded in DECISIONS.md. Bands above Nyquist/limit for low sample rates are bypassed.

Why not `android.media.audiofx.Equalizer`: band count and quality vary by device (often ~5 bands), no control over curve. Why not `android.media.audiofx.Visualizer`: requires `RECORD_AUDIO` permission and is device-dependent. (ADR-0005, ADR-0006)

Audio-thread rules: preallocate everything in `configure()`/`onFlush`; no allocation, locks, logging, or blocking I/O in `queueInput`. Denormals: add tiny DC offset or flush to zero.

## 5. Spectrum / oscilloscope
- Analysis thread (not audio thread) reads the ring buffer at ~30–60 Hz, applies Hann window, FFT size 2048 (configurable), maps to 16–32 log-spaced bands, applies attack/decay smoothing and peak hold.
- Publishes `SpectrumFrame(bands: FloatArray, peaks: FloatArray, waveform: FloatArray)` through a conflated flow. The array pair is double-buffered to avoid per-frame allocation.
- UI draws in Compose `Canvas`/`drawWithCache`; read frame inside the draw phase (via snapshot state) to avoid recomposition per frame.
- Fullscreen shaders (P3): `GLSurfaceView`/`TextureView` with GLSL fragment shaders fed with spectrum as a texture/uniform array.
- Analysis runs only while the visualizer UI is visible and playing. Stop when backgrounded.

## 6. Data
Room database `ampere.db` (migrations required from first release; export schema JSON into `app/schemas/`).

Entities (initial; fields may grow):
- `TrackEntity(id, mediaStoreId, uri, title, artistId, albumId, genre, durationMs, trackNo, discNo, year, mime, bitrate, sampleRate, dateAdded, dateModified, rating, playCount, lastPlayedAt, rgTrackGain, rgAlbumGain, missing)`
- `ArtistEntity(id, name, sortName)`, `AlbumEntity(id, title, artistId, year, artUri)`
- `PlaylistEntity(id, name, createdAt, updatedAt)`, `PlaylistEntryEntity(playlistId, position, trackId)`
- `EqPresetEntity(id, name, bands(10), preamp, builtIn)`
- `QueueStateEntity` (single row: items, index, positionMs, shuffle, repeat)
- `StationEntity(id, name, url, favicon, tags, favorite)` (P4)
- `PodcastEntity`, `EpisodeEntity` (P4)

DataStore (Preferences): selected skin id, EQ enabled/preset id, crossfade ms, ReplayGain mode, scan folders, theme options.

Scanner: query `MediaStore.Audio.Media` (+ folder filters) in pages; diff against DB by `mediaStoreId` + `dateModified`; mark missing, don't delete immediately; extra tags (ReplayGain, lyrics) read lazily with a tag reader chosen in P1 (ADR pending, Apache/MIT/BSD only).

## 7. Skins
Skin = immutable `SkinPack` loaded from bundled assets or an imported `.ampskin` file, exposed as `LocalSkin` CompositionLocal with tokens (colors, shapes, typography, LCD config, analyzer style, button style/sprites). Full format: UI_SPEC.md §6. Parser/validator lives in `skin/`; it must be pure and unit-tested with malicious-input cases.

## 8. Permissions and manifest
- `READ_MEDIA_AUDIO` (API 33+), `READ_EXTERNAL_STORAGE` (≤ API 32)
- `POST_NOTIFICATIONS` (API 33+), request in context
- `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_MEDIA_PLAYBACK`
- `INTERNET` (P4 features only; declare from start for simplicity or add in P4 — decide in P4)
- NOT requested: `RECORD_AUDIO`, location, contacts.
- Service: `android:foregroundServiceType="mediaPlayback"`, `exported` per Media3 docs.

## 9. Concurrency
Kotlin coroutines + Flow. `viewModelScope` in UI, a service-scoped `CoroutineScope(SupervisorJob() + Dispatchers.Default)` in playback, IO dispatcher for DB/files. No `GlobalScope`. Dedicated single thread for analysis.

## 10. DI
Manual constructor injection through `AppContainer`, created by `AmpereApplication` (ADR-0021). ViewModels get dependencies through small `ViewModelProvider.Factory` classes. `:dsp` has no DI; constructed by `:app`. Revisit Hilt when wiring becomes painful and its AGP 9 / KSP compatibility is verified.

## 11. Testing strategy
- `:dsp`: JVM unit tests — biquad coefficients vs reference values, frequency response (feed sine, measure gain), FFT vs known signals, ring buffer concurrency.
- Data: Room in-memory tests, scanner diff logic with fakes, M3U/PLS parser tests (incl. weird paths/encodings).
- Skin parser: valid, invalid, zip-slip, oversize, missing-asset cases.
- Playback: instrumented smoke tests on emulator/device (play fixture file, seek, queue).
- UI: Compose previews for each screen and each bundled skin; a few Compose UI tests for key flows.
- Manual audio checklist per release in RELEASE.md (gapless album, EQ sweep, 2-hour soak).

## 12. Performance budgets
See SPEC.md §8. Additionally: scanner must not hold the DB open in one giant transaction; lists use stable keys; images via Coil with size-bounded decoding; analyzer allocates nothing per frame.

## 13. Security and privacy
- No analytics/telemetry in v1. Network only for radio/podcasts (P4).
- Skin import: validate manifest, cap file count/size/decoded image dimensions, reject path traversal (zip-slip), never execute code from packs.
- Cleartext HTTP is needed for many radio streams: use a network security config scoped as narrowly as practical, documented in an ADR in P4.
- Keystore and credentials never in the repo.
