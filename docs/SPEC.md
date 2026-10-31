# SPEC — Product specification

Status: v0.1 (2026-10-31). Source of truth for *what* we build. *How* is in ARCHITECTURE.md; *when* is in PHASES.md.

## 1. Vision
A fast, local-first Android music player for people who like their player to feel like an instrument panel: LCD readout, real 10-band EQ, live spectrum, deep playlist control, and skins. Retro soul, modern touch UX.

## 2. Target users
- Owns a local music collection (MP3/FLAC files) and dislikes streaming-only apps.
- Wants control: EQ, gapless, queue editing, tags, smart playlists.
- Nostalgic for desktop-player customization (skins, visualizers).

## 3. Principles
1. Local files first. Works fully offline, no account.
2. Audio quality and reliability beat features. Playback must never glitch.
3. Everything visual is skinnable.
4. No ads, no tracking, no analytics in v1.
5. Touch-first: 48dp targets, one-handed reachable controls.

## 4. Non-goals (v1)
Video playback, CD ripping/burning, audio encoding/conversion, streaming-service integration (Spotify, etc.), cloud sync, accounts, iOS, desktop, an online skin store, loading third-party code plugins.

## 5. Features
Priority = phase in which it ships (P1..P4, see PHASES.md). Each feature has an ID used in commits and tasks.

### Playback (F-PLAY)
| ID | Feature | Pri | Acceptance criteria |
|---|---|---|---|
| F-PLAY-01 | Play/pause/stop/next/prev/seek | P1 | All work from app, notification, lock screen, Bluetooth/headset buttons |
| F-PLAY-02 | Background playback | P1 | Keeps playing with screen off; survives app swipe-away; proper foreground service + media notification |
| F-PLAY-03 | Formats | P1 | MP3, AAC/M4A, FLAC, OGG Vorbis, Opus, WAV play correctly |
| F-PLAY-04 | Shuffle + repeat (off/all/one) | P1 | State persists across restarts |
| F-PLAY-05 | Resume | P1 | Restores queue, track, and position after app/process restart |
| F-PLAY-06 | Gapless | P2 | No audible gap between consecutive tracks of gapless-capable formats (test album with continuous tracks) |
| F-PLAY-07 | Crossfade | P2 | Configurable 0–12 s; off by default; no clicks; research spike first (Risk R1) |
| F-PLAY-08 | ReplayGain | P2 | Reads track/album gain tags; applies via preamp stage; user toggle (off/track/album) |
| F-PLAY-09 | Playback speed | P2 | 0.5x–2.0x, pitch-preserving |
| F-PLAY-10 | Sleep timer | P2 | Minutes or end-of-track; fades out over last 5 s |
| F-PLAY-11 | Audio focus + noisy events | P1 | Pauses on unplug/call; ducks for notifications; resumes per Android guidance |

### Queue and playlists (F-LIST)
| ID | Feature | Pri | Acceptance criteria |
|---|---|---|---|
| F-LIST-01 | Queue view | P1 | Reorder by drag, swipe to remove, play next, add to end, jump to track |
| F-LIST-02 | Saved playlists | P1 | Create/rename/delete; add tracks from anywhere; stored in Room |
| F-LIST-03 | M3U/M3U8 import + export | P1 | Round-trips; relative and absolute paths resolved against MediaStore; missing files reported, not fatal |
| F-LIST-04 | PLS import + export | P2 | As above |
| F-LIST-05 | Smart playlists | P2 | Built-ins: Most played, Recently added, Recently played, Never played, Top rated |
| F-LIST-06 | Playlist sort + dedupe | P2 | Sort by title/artist/album/duration/date; remove duplicates |

### Library (F-LIB)
| ID | Feature | Pri | Acceptance criteria |
|---|---|---|---|
| F-LIB-01 | MediaStore scan | P1 | Indexes audio into Room; 10,000 tracks scan in a reasonable time without blocking UI; incremental rescan |
| F-LIB-02 | Browse by artist/album/genre/folder/all | P1 | Fast scrolling lists, alphabetical index |
| F-LIB-03 | Search | P1 | Instant (as-you-type) across title/artist/album |
| F-LIB-04 | Album art | P1 | Embedded or folder art; cached; placeholder if none |
| F-LIB-05 | Ratings + play counts | P2 | 1–5 stars, stored in app DB (not written to files); play count increments after 50% or 4 min |
| F-LIB-06 | Tag viewer | P2 | Shows all readable tags |
| F-LIB-07 | Tag editor | P3 | Edits title/artist/album/year/genre/track; handles scoped-storage write consent (Risk R3) |
| F-LIB-08 | Lyrics | P2 | Embedded lyrics and sidecar `.lrc`; synced highlight when timestamps exist |

### Equalizer and DSP (F-EQ)
| ID | Feature | Pri | Acceptance criteria |
|---|---|---|---|
| F-EQ-01 | 10-band EQ | P2 | Bands at 60, 170, 310, 600, 1k, 3k, 6k, 12k, 14k, 16k Hz; ±12 dB each; real-time, no zipper noise |
| F-EQ-02 | Preamp | P2 | ±12 dB; clip protection (auto-reduce or soft limiter) |
| F-EQ-03 | Presets | P2 | Built-ins (Flat, Rock, Pop, Jazz, Classical, Bass boost, Treble boost, Vocal, Dance); user presets save/rename/delete |
| F-EQ-04 | EQ on/off + per-output memory | P2 | Toggle is instant; optionally remember preset per output device (speaker/headphones/Bluetooth) |
| F-EQ-05 | Balance | P2 | -100..+100 |

### Visuals (F-VIZ)
| ID | Feature | Pri | Acceptance criteria |
|---|---|---|---|
| F-VIZ-01 | Spectrum analyzer | P2 | Shown on main player; log-spaced bars; smooth falloff and peak hold; ≤5% CPU on mid-range device (target) |
| F-VIZ-02 | Oscilloscope | P2 | Waveform view selectable on main player |
| F-VIZ-03 | Fullscreen visualizer | P3 | Shader-based, 30–60 fps, tap to cycle presets |
| F-VIZ-04 | Visualizer presets as data | P3 | Presets are JSON + GLSL text, bundled and importable; no code execution beyond GLSL |
| F-VIZ-05 | Milkdrop-style engine | P4 (stretch) | Evaluate projectM or custom; license review required first |

### Skins (F-SKIN)
| ID | Feature | Pri | Acceptance criteria |
|---|---|---|---|
| F-SKIN-01 | Skin engine | P3 | Whole UI restyles live from a skin; defined in UI_SPEC.md §6 |
| F-SKIN-02 | Bundled skins | P3 | At least 4 original skins: Classic LCD (default), Light, Amber terminal, Minimal dark |
| F-SKIN-03 | Import skin pack | P3 | `.ampskin` zip; validated, size-limited, sandboxed (UI_SPEC.md §6.4) |
| F-SKIN-04 | Classic `.wsz` importer | P4 (stretch) | Converts classic bitmap skins to an in-memory skin; best effort; spike first (Risk R4) |
| F-SKIN-05 | Dynamic color option | P3 | Optional Material You derived theme as one more "skin" |

### Connected (F-NET)
| ID | Feature | Pri | Acceptance criteria |
|---|---|---|---|
| F-NET-01 | Internet radio playback | P4 | Plays Icecast/SHOUTcast HTTP streams; shows stream metadata (now playing); reconnect on drop |
| F-NET-02 | Station directory + favorites | P4 | Search/browse via Radio Browser API; favorites stored locally |
| F-NET-03 | Podcasts | P4 | Search via Podcast Index API; subscribe; episode list; resume position; download optional |
| F-NET-04 | Custom stream URL | P4 | User can add any stream URL |

### System integration (F-SYS)
| ID | Feature | Pri | Acceptance criteria |
|---|---|---|---|
| F-SYS-01 | Media notification | P1 | Art, title, transport controls; works with lock screen |
| F-SYS-02 | Home-screen widget | P4 | Art, title, play/pause, next |
| F-SYS-03 | Android Auto | P4 | Browsable library, play via car UI; passes Desktop Head Unit test |
| F-SYS-04 | Open with / share intent | P2 | Playing a tapped audio file from a file manager works and queues it |
| F-SYS-05 | Settings | P1 | Theme/skin, scan folders, audio options, about + licenses screen |
| F-SYS-06 | Backup/export | P4 | Export playlists, ratings, EQ presets, settings to a file; import back |

## 6. Adaptations from desktop (decided)
| Desktop concept | Android equivalent |
|---|---|
| Docked windows (player, EQ, playlist, library) | Screens, bottom sheets, tabs |
| Global hotkeys | Media session + headset/Bluetooth buttons |
| Mini mode / windowshade | Mini-player bar + widget |
| Code plugins (DSP, vis, general) | Data packs (skins, visualizer presets, EQ presets) |
| Media Library database | Room DB over MediaStore |
| CD rip/burn | Dropped |

## 7. Legal and naming
Clean-room (docs/CLEAN_ROOM.md). Name, icon, art, and skins are original. Working codename "Ampere" is a placeholder; run a trademark/store-name check before public release (open question in HANDOFF.md).

## 8. Quality targets (initial; revisit after P2)
- Cold start to interactive UI: ≤ 1.5 s on a mid-range phone.
- No dropped frames on player screen with analyzer running (target 60 fps; accept 30 on low-end).
- Zero audible glitches in a 2-hour soak test with EQ on.
- App size: aim < 25 MB APK.
- Crash-free sessions ≥ 99.5% (when measured).
