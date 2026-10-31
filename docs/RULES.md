# RULES

Binding for every contributor, human or AI. If a rule is wrong, change it here via a DECISIONS.md entry — don't silently break it.

## 1. Process
1. Work one task ID at a time. Small commits. Reference the ID.
2. `main` always builds and passes CI. Work on short-lived branches (`feat/p1-04-playback-service`) and merge via PR (squash) when the owner wants review, or direct to `main` for docs-only changes.
3. Phase exits are approval gates.
4. Any architectural change, new dependency, or format/schema change → DECISIONS.md entry *before* the code.
5. End every session by updating HANDOFF.md. See CLAUDE.md.
6. Don't claim something works unless it was run. State what was and wasn't tested.

## 2. Git
- Conventional Commits: `feat|fix|refactor|test|docs|build|ci|chore(scope): summary`. Scope examples: `eq`, `playback`, `library`, `skin`, `ui`, `dsp`.
- Body mentions task ID: `Refs: P2-02`.
- Tags: `vMAJOR.MINOR.PATCH` (pre-releases `v0.2.0-rc.1`). Tag only from `main`.
- Never commit: keystores, `keystore.properties`, `.env`, `local.properties`, build outputs, large binaries (use Git LFS only if truly needed, ADR first).

## 3. Kotlin and code style
- Kotlin official style, enforced by Spotless/ktlint and detekt. Max line 120.
- Prefer immutable data classes, sealed types for state/events, explicit visibility (`internal` by default for non-API).
- No `!!` outside tests. No `GlobalScope`. No blocking calls on the main thread.
- Public functions in `:dsp` have KDoc with units (Hz, dB, samples) and the source of the algorithm.
- Comments explain *why*. Keep functions short.
- Strings in `strings.xml` (all user-facing text; sentence case). No hardcoded colors/sizes in UI — tokens only.

## 4. Architecture rules
- Layering and package rules from ARCHITECTURE.md §2 are enforced in review.
- ViewModels hold no `Context`; expose `StateFlow<UiState>` and take events via functions. One-shot effects via `Channel`/`SharedFlow`, not state flags.
- Compose: hoist state; stateless composables where possible; every screen has `@Preview`s (default + at least one other skin once P3 lands); avoid recomposition per animation frame (read volatile state in draw phase).
- Repositories return domain models, not entities.
- DB schema changes need a Room migration + test. Export schemas to `app/schemas/`.

## 5. Audio rules (hard)
- No allocation, locks, logging, or I/O in `AudioProcessor.queueInput` and callees.
- DSP code in `:dsp` is deterministic and unit-tested with numeric tolerance.
- Never change gain staging without testing clipping with a full-scale sine and max boost.
- EQ/limiter changes must be A/B listened on real hardware before merge; note result in HANDOFF.md.

## 6. Testing
- Required: `:dsp` (all), parsers (M3U/PLS/skin), scanner diff logic, repositories with Room in-memory.
- UI: previews + manual check noted. Add Compose UI tests for critical flows (play, queue edit, EQ change) from P2.
- Bugs get a regression test when feasible.
- Fixtures in `app/src/androidTest/assets` or `dsp/src/test/resources`; only use audio files we own or that are CC0/public domain; log them in ASSETS.md.

## 7. Dependencies
- All versions in `gradle/libs.versions.toml`. Pin exact versions. Check the official release notes — don't guess versions.
- Allowed licenses: Apache-2.0, MIT, BSD, ISC. Others (LGPL/GPL/AGPL/unknown) need an ADR first.
- Prefer AndroidX/Jetpack and Kotlin ecosystem libs. Keep the dependency count small.
- Run a dependency update review at each phase exit.

## 8. Security and privacy
- No secrets in the repo, logs, or screenshots.
- Treat skin/playlist/podcast-feed input as hostile: validate sizes, paths, encodings.
- No telemetry/analytics without an ADR and an in-app opt-in.
- Release signing key: stored offline backup + GitHub Actions secrets. Losing it means users can't update in place.

## 9. UI/UX
- 48dp min targets; labels for icon buttons; dark and light handled by skin `dark` flag.
- Respect reduce-motion and font scale.
- Sentence case for all UI text, no exclamation marks in system copy.
- Every list has loading, empty, and error states.

## 10. Docs
- SPEC (what), ARCHITECTURE (how), PHASES (when), DECISIONS (why), HANDOFF (now). Keep each in its lane.
- When behavior changes, update the doc in the same commit.
- New assets → ASSETS.md with license + source.
