# CLAUDE.md — Ampere (working codename)

Native Android music player inspired by Winamp's feature set: 10-band EQ, spectrum analyzer, skins, playlists, radio. Kotlin + Jetpack Compose + Media3. Built clean-room (see docs/CLEAN_ROOM.md). Released through GitHub Actions.

This file is the entry point. Read it fully at the start of every session.

## Read order (every session)
1. `CLAUDE.md` (this file)
2. `docs/HANDOFF.md` — current state, what's next, open questions
3. The phase you are in: `docs/PHASES.md`
4. Only as needed: `docs/SPEC.md`, `docs/ARCHITECTURE.md`, `docs/UI_SPEC.md`, `docs/RULES.md`, `docs/DECISIONS.md`, `docs/RELEASE.md`

## Hard rules (short form — full text in docs/RULES.md)
1. Clean-room. Never copy, paste, port, or consult Winamp source code, assets, skins, or name. No llama. See docs/CLEAN_ROOM.md.
2. One task ID at a time (e.g. `P1-04`). One task = one small commit. `main` must always build.
3. Never make an architectural or dependency decision silently. Add an entry to `docs/DECISIONS.md` first.
4. Audio thread rules: no allocation, no locks, no logging, no I/O inside `AudioProcessor.queueInput` or anything it calls.
5. UI never touches ExoPlayer directly. UI talks to `MediaController` through the playback repository.
6. `:dsp` module is pure Kotlin/JVM. No `android.*` imports there, ever.
7. No secrets in the repo. Keystore and passwords live only in GitHub Actions secrets.
8. Skins and visualizer presets are data, never code. No dynamic code loading.
9. If the spec and the code disagree, stop and flag it in HANDOFF.md. Do not guess which is right.
10. Do not invent library versions or API names. Check the official docs or the Gradle catalog first.

## Commands
Available once P0-02 (Gradle project) and P0-04 (lint/format) are done:
```
./gradlew assembleDebug            # debug APK
./gradlew testDebugUnitTest        # unit tests (includes :dsp)
./gradlew :dsp:test                # DSP tests only (fast, JVM)
./gradlew spotlessCheck detekt     # format + static analysis
./gradlew lintDebug                # Android lint
```
Run `spotlessApply` before committing. CI runs the same checks (docs/RELEASE.md).

## Where things live
```
app/            Android app (UI, service, data) — package site.yatharthwebs.ampere
dsp/            Pure Kotlin: biquad EQ, FFT, window functions, ring buffer
docs/           All project docs (this list)
.github/        CI + release workflows
```
Package layout inside `app/` is in docs/ARCHITECTURE.md §2.

## Session protocol
Start: read order above → confirm current phase and next task from HANDOFF.md → restate the task in one sentence → do it.
During: small steps, build/test after each, commit with Conventional Commits (`feat(eq): add biquad peaking filter`), reference the task ID.
End (always, even if the session was short):
- Update `docs/HANDOFF.md`: what was done, what's next, anything half-finished, new open questions.
- Tick finished tasks in `docs/PHASES.md`.
- Add any new decision to `docs/DECISIONS.md`.
- If you could not run or test something, say so in HANDOFF.md. Never claim "tested" for what wasn't run.

## Definition of done (a task)
- Builds, existing tests pass, new logic has tests (DSP and data layers: required; UI: preview + manual check noted).
- Lint/format clean.
- Acceptance criteria in docs/SPEC.md for the related F-ID are met, or the gap is written in HANDOFF.md.
- Docs touched if behavior or structure changed.

## Collaboration style
- Phase exits are approval gates: stop at the end of each phase, summarize, and wait for the owner's go-ahead.
- When something is ambiguous and the answer changes the design, ask one clear question. Otherwise pick the simplest option, record it in DECISIONS.md, and continue.
- Be honest about risk. Crossfade, tag editing on scoped storage, and the `.wsz` importer are known hard spots (docs/PHASES.md → Risks).
