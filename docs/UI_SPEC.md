# UI_SPEC

Status: v0.1 (2026-10-31). Visual concept: retro-modern. A dark LCD readout and chunky transport controls, generous touch targets, everything skinnable.

## 1. Principles
- 48dp minimum touch target (spec: 56dp for primary transport).
- One-handed: primary controls in the lower half.
- LCD aesthetic through *tokens* (colors, mono font, pixel-ish bars), not hardcoded.
- Every screen renders in every bundled skin; each screen has Compose previews per skin.
- Accessibility: content descriptions on all icon buttons, TalkBack order sensible, contrast ≥ 4.5:1 for text in bundled skins, respects font scale and reduce-motion.

## 2. Navigation
Bottom bar (5): Library · Playlist · **Now playing** (center) · Radio · Settings. Radio appears in P4; before that the bar has 4 items.
Persistent mini player above the bar on all screens except Now playing. Tapping it opens Now playing.
EQ is a bottom sheet reachable from Now playing (and Settings → Audio). Skin picker lives under Settings → Appearance.

## 3. Screens

### 3.1 Now playing (main player)
- Top bar: collapse, "Now playing", overflow (sleep timer, speed, lyrics, tag info).
- LCD panel: elapsed time (large), remaining/total toggle on tap, title (marquee when long), artist — album, tech line (`320 kbps 44.1 kHz stereo`), small indicators (shuffle/repeat/EQ on).
- Visual area inside LCD: spectrum analyzer by default; tap cycles analyzer → oscilloscope → album art. Long-press opens fullscreen visualizer (P3).
- Seek bar (below LCD), drag with time bubble.
- Transport row: prev, play, pause, stop, next (Winamp-style five). Play/pause may merge into one toggle in a skin that prefers it (skin flag).
- Toggle row: shuffle, repeat, EQ, queue, skin quick-switch.
- Volume + balance row (system volume integration; balance is app-level).
- Swipe up: queue sheet.

### 3.2 Library
Tabs: Songs, Albums, Artists, Genres, Folders. Search bar on top (instant). Fast-scroll alphabetical index. Long-press = multi-select (add to queue, add to playlist, play next, rate). Row: art, title, artist, duration. Smart playlists section at top of Playlist tab.

### 3.3 Playlist / Queue
- Queue: current track highlighted, drag handle, swipe to remove, header with count + total time, toolbar: add, sort, save as playlist, clear.
- Saved playlists: list → detail (same row component), import/export M3U/PLS from overflow.

### 3.4 Equalizer (sheet)
10 vertical sliders with dB readout, preamp slider, preset chip + menu (select/save/rename/delete), on/off switch, reset, balance. Band labels: 60, 170, 310, 600, 1k, 3k, 6k, 12k, 14k, 16k. Live response curve above sliders (optional P3).

### 3.5 Radio (P4)
Favorites, search, browse by tag/country. Now-playing shows stream metadata. "Add stream URL" action.

### 3.6 Settings
Appearance (skin, dynamic color, analyzer style), Audio (ReplayGain, crossfade, gapless info, EQ per-output), Library (folders, rescan, ignore short tracks), Backup (P4), About (version, licenses, "inspired by" note).

### 3.7 Skin picker
Grid of previews of the current screen in each skin, tap to apply live, import button (`.ampskin`).

## 4. Components (shared)
`LcdPanel`, `Analyzer`, `Oscilloscope`, `SeekBar`, `TransportButton`, `ToggleChip`, `EqSlider`, `TrackRow`, `MiniPlayer`, `SectionHeader`, `EmptyState`. Each takes colors/shapes only from `LocalSkin`.

## 5. Design tokens (v1)
Defined once in `ui/theme/SkinTokens.kt` and populated by a skin.
- Colors: `background`, `surface`, `surfaceVariant`, `onSurface`, `onSurfaceMuted`, `accent`, `onAccent`, `outline`, `danger`, `lcdBackground`, `lcdForeground`, `lcdDim`, `analyzerLow`, `analyzerMid`, `analyzerHigh`, `analyzerPeak`, `selectionBg`.
- Shape: `cornerSmall`, `cornerMedium`, `cornerLarge` (dp), `outlineWidth`.
- Typography: `fontUi` (family), `fontLcd` (mono/pixel family), size scale (caption, body, title, lcdLarge).
- Controls: `transportStyle` (`outline` | `filled` | `sprite`), `sliderStyle` (`line` | `block` | `sprite`), `analyzerStyle` (`bars` | `segmented` | `line` | `dots`), `barCount`, `peakHold` (bool), `mergePlayPause` (bool).
- Motion: `reduceMotion` honored globally.

## 6. Skin pack format (`.ampskin`, schemaVersion 1)
A zip file. Data only.

### 6.1 Layout
```
skin.json              required manifest
fonts/*.ttf|otf        optional (OFL/Apache only for bundled)
images/*.png|webp      optional (sprites, backgrounds)
preview.png            optional, ≤ 512x512
```

### 6.2 `skin.json`
```json
{
  "schemaVersion": 1,
  "id": "classic-lcd",
  "name": "Classic LCD",
  "author": "Ampere",
  "version": "1.0.0",
  "license": "CC0-1.0",
  "dark": true,
  "colors": {
    "background": "#0F1115", "surface": "#171A21", "surfaceVariant": "#1F232C",
    "onSurface": "#E6E8EE", "onSurfaceMuted": "#9AA1B2",
    "accent": "#5EE07A", "onAccent": "#06210F", "outline": "#2B3040", "danger": "#FF5C5C",
    "lcdBackground": "#0D1410", "lcdForeground": "#5EE07A", "lcdDim": "#3F9E55",
    "analyzerLow": "#5EE07A", "analyzerMid": "#C9E85E", "analyzerHigh": "#FF7A5C", "analyzerPeak": "#FFFFFF",
    "selectionBg": "#1E3A28"
  },
  "shape": { "cornerSmall": 4, "cornerMedium": 8, "cornerLarge": 16, "outlineWidth": 1 },
  "typography": { "fontUi": "system", "fontLcd": "fonts/lcd.ttf" },
  "controls": {
    "transportStyle": "outline", "sliderStyle": "block", "analyzerStyle": "segmented",
    "barCount": 24, "peakHold": true, "mergePlayPause": false
  }
}
```
Color format `#RRGGBB` or `#AARRGGBB`. Unknown keys ignored (forward compat). Missing keys fall back to the default skin's value.

### 6.3 Sprite mode (P3+, optional)
`transportStyle: "sprite"` reads `images/transport.png` with a `sprites` block mapping control → rect (x, y, w, h) per state (normal, pressed, disabled). Scaled with nearest-neighbor when `pixelArt: true`.

### 6.4 Validation and limits (enforced by parser)
- Max zip size 5 MB; max 64 entries; max single file 2 MB; decoded image ≤ 2048×2048.
- Reject entries with `..`, absolute paths, or symlinks (zip-slip).
- Only allowed extensions: json, png, webp, ttf, otf.
- `id` must match `^[a-z0-9][a-z0-9-]{1,40}$`.
- Parsing happens off the main thread; failure shows a readable error, never crashes.
- No scripts, no expressions, no network access from a skin.

### 6.5 Visualizer presets (P3)
JSON + GLSL fragment source (text). Inputs: `uSpectrum[]`, `uWaveform[]`, `uTime`, `uResolution`, `uBeat`. Size-capped; compiled with error capture; failing presets are skipped.

### 6.6 `.wsz` import (P4 stretch)
Classic skins are zip files of bitmaps. Importer maps their sprite sheets to our sprite mode on a best-effort basis. Only import user-supplied skins; never bundle them. Spike first (Risk R4).

## 7. Bundled skins (P3)
1. Classic LCD (default): dark, green LCD.
2. Paper: light, high contrast, serif-free sans.
3. Amber terminal: amber on black, scanline-free.
4. Minimal dark: neutral gray, thin lines.
(Plus "Dynamic color" via Material You as a system skin.)

## 8. Empty/error/loading states
Every list has an empty state with an action ("No music found — choose folders"). Permission denied: explain + button to settings. Scan in progress: non-blocking progress chip. Playback error: toast/snackbar with skip.
