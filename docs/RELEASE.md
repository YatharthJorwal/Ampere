# RELEASE — CI, signing, and GitHub Releases

## 1. Workflows
- `.github/workflows/ci.yml` — on push to `main` and on PRs: format check, lint, unit tests, debug build. Build steps are skipped until `gradlew` exists (so the docs-only first commit doesn't fail).
- `.github/workflows/release.yml` — on tag `vX.Y.Z[-suffix]`: builds signed release APK + AAB, writes `SHA256SUMS.txt`, creates a GitHub Release with auto-generated notes. Tags containing `-` (e.g. `v0.2.0-rc.1`) are marked pre-release.

Action versions in the YAML (`checkout@v4`, `setup-java@v4`, `gradle/actions/setup-gradle@v4`, `softprops/action-gh-release@v2`) were current when written; check for newer majors when you set this up.

## 2. Versioning
- SemVer tags: `v0.1.0`, `v0.2.0-rc.1`, `v1.0.0`.
- `versionName` = tag without `v`. `versionCode` = GitHub run number (monotonically increasing). Both passed as Gradle properties (`-PversionName`, `-PversionCode`); local builds fall back to `0.0.0-dev` / `1`.

## 3. Signing (one-time setup)
Signing is implemented in `app/build.gradle.kts`: if the environment variable `KEYSTORE_PATH` is set, the release build is signed with `KEYSTORE_PASSWORD`, `KEY_ALIAS` and `KEY_PASSWORD`; otherwise it builds unsigned (fine for local checks). Version comes from `-PversionName` / `-PversionCode` (defaults `0.0.0-dev` / `1`).

1. Create a keystore on your PC, **outside the repo** (Windows example; Android Studio ships a `keytool`):
   ```powershell
   $keytool = "C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe"
   New-Item -ItemType Directory -Force E:\ampere-keys | Out-Null
   & $keytool -genkeypair -v -keystore E:\ampere-keys\ampere-release.jks -alias ampere -keyalg RSA -keysize 4096 -validity 10000
   ```
   Choose a strong password and store it in a password manager. Press Enter to reuse it as the key password. Name/organization questions can be answered loosely; confirm with `yes`.
2. **Back up the `.jks` file** in at least two safe places. If you lose it, installed users can't update in place; if it leaks, anyone can sign as you. Never paste the file or password into chat, issues or commits (`*.jks` is git-ignored).
3. Copy it as base64 to the clipboard:
   ```powershell
   [Convert]::ToBase64String([IO.File]::ReadAllBytes("E:\ampere-keys\ampere-release.jks")) | Set-Clipboard
   ```
4. GitHub → repo → Settings → Secrets and variables → Actions → New repository secret. Add four:
   - `KEYSTORE_BASE64` (paste from the clipboard)
   - `KEYSTORE_PASSWORD`
   - `KEY_ALIAS` = `ampere`
   - `KEY_PASSWORD` (same as the keystore password if you pressed Enter)

## 4. Cutting a release
1. Make sure `main` is green and HANDOFF.md/PHASES.md are updated.
2. Run the manual checklist (§5) on a real device with a debug or locally built release APK.
3. Tag and push:
   ```
   git checkout main && git pull
   git tag v0.1.0
   git push origin v0.1.0
   ```
4. Watch the Actions run; open the Release page; download the APK, verify with `sha256sum -c SHA256SUMS.txt`, install and smoke test.
5. If broken: delete the release and tag (`git push --delete origin v0.1.0`), fix, retag with a new patch version. Never reuse a version code.

## 5. Manual release checklist
- [ ] Fresh install: permissions flow works, scan finds music
- [ ] Play MP3, FLAC, AAC, OGG, Opus, WAV
- [ ] Background playback 30+ min with screen off; notification controls; Bluetooth/headset buttons
- [ ] Gapless album test (P2+)
- [ ] EQ sweep: each band audibly affects its range; no clicks when dragging; max boost doesn't clip (P2+)
- [ ] Analyzer smooth, app doesn't heat the phone (P2+)
- [ ] Upgrade install over previous release keeps library/playlists/settings
- [ ] Rotate, dark/light, large font
- [ ] About screen licenses present; no Winamp marks (CLEAN_ROOM.md checklist)

## 6. Distribution notes
- APK sideloading requires "install unknown apps" on the phone.
- The AAB is for Google Play if you decide to publish there (needs a developer account, privacy policy, target API compliance at that time).
- Optionally add F-Droid later (needs reproducible/open-source license; see ADR-0014).
