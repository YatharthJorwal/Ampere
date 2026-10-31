# CLEAN_ROOM — rules for building a Winamp-inspired app safely

Why: Winamp's desktop source is public but under a source-available license that restricts redistributing modified versions, and the Winamp name, logo and llama art are trademarks. We build our own app from scratch, independently.

## Allowed
- Using the *idea level*: features, user-visible behavior, general UX conventions (e.g. "a 10-band EQ with a preamp", "skins", "playlist files").
- Using public file-format and protocol specs: M3U/PLS, ID3/Vorbis/FLAC tags, ReplayGain, ICY/Icecast/SHOUTcast metadata, RBJ audio EQ cookbook formulas.
- Reading independent, permissively licensed projects for format knowledge (e.g. an MIT-licensed reimplementation of the classic skin format), after checking the license, and noting it in DECISIONS.md.
- Describing Winamp behavior from using the app or reading public reviews/docs.

## Not allowed
- Reading, copying, translating, or "porting" code from the Winamp source repository (or forks/leaks of it).
- Using any Winamp/Llama Group logo, icon, llama, name, window art, bundled skins, sounds, or text strings.
- Shipping or bundling third-party skins without clear permission/license. Classic skins on the web are mostly copyrighted by their authors.
- Naming anything "Winamp", "WA", "Nullsoft", "Llama", or implying affiliation.

## Practice
- If you (human or AI) have read Winamp source code, say so in HANDOFF.md and do not work on the component that code relates to.
- When implementing an algorithm, cite its public source in a code comment (e.g. "RBJ Audio EQ Cookbook, peaking EQ").
- All bundled art, fonts, and skins must have a recorded license in `docs/ASSETS.md` (create at first asset).
- Fonts: use OFL/Apache-licensed fonts only.
- Dependencies: Apache-2.0, MIT, BSD OK. LGPL/GPL/AGPL/unknown → needs a DECISIONS.md entry first (LGPL has dynamic-linking and relinking implications on Android).

## Release check (before any public release)
- [ ] Name and icon clear of third-party marks
- [ ] All assets have recorded licenses
- [ ] About screen lists open-source licenses
- [ ] No Winamp references except an "inspired by" mention in docs
