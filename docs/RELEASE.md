# RELEASE — CI, signing, and GitHub Releases

## 1. Workflows
- `.github/workflows/ci.yml` — on push to `main` and on PRs: format check, lint, unit tests, debug build. Build steps are skipped until `gradlew` exists (so the docs-only first commit doesn't fail).
- `.github/workflows/release.yml` — on tag `vX.Y.Z[-suffix]`: builds signed release APK + AAB, writes `SHA256SUMS.txt`, creates a GitHub Release with auto-generated notes. Tags containing `-` (e.g. `v0.2.0-rc.1`) are marked pre-release.

Action versions in the YAML (`checkout@v4`, `setup-java@v4`, `gradle/actions/setup-gradle@v4`, `softprops/action-gh-release@v2`) were current when written; check for newer majors when you set this up.

## 2. Versioning
- SemVer tags: `v0.1.0`, `v0.2.0-rc.1`, `v1.0.0`.
- `versionName` = tag without `v`. `versionCode` = GitHub run number (monotonically increasing). Both passed as Gradle properties (`-PversionName`, `-PversionCode`); local builds fall back to `0.0.0-dev` / `1`.

## 3. Signing (one-time setup)
1. Create a keystore on your machine (keep it OUT of the repo):
   ```
   keytool -genkeypair -v -keystore release.jks -alias ampere \
     -keyalg RSA -keysize 4096 -validity 10000
   ```
2. **Back it up** in at least two safe places (password manager + offline drive). If you lose it, installed users can't update in place; if it leaks, anyone can sign as you.
3. Base64 it: `base64 -w0 release.jks` (macOS: `base64 -i release.jks | tr -d '\n'`).
4. GitHub → repo → Settings → Secrets and variables → Actions → add:
   - `KEYSTORE_BASE64` (the base64 text)
   - `KEYSTORE_PASSWORD`
   - `KEY_ALIAS` (e.g. `ampere`)
   - `KEY_PASSWORD`
5. Add the signing config in `app/build.gradle.kts` (task P0-08). Reads env vars in CI and optional `keystore.properties` locally:
   ```kotlin
   android {
       defaultConfig {
           versionName = (project.findProperty("versionName") as String?) ?: "0.0.0-dev"
           versionCode = (project.findProperty("versionCode") as String?)?.toInt() ?: 1
       }
       signingConfigs {
           create("release") {
               val path = System.getenv("KEYSTORE_PATH")
               if (path != null) {
                   storeFile = file(path)
                   storePassword = System.getenv("KEYSTORE_PASSWORD")
                   keyAlias = System.getenv("KEY_ALIAS")
                   keyPassword = System.getenv("KEY_PASSWORD")
               }
           }
       }
       buildTypes {
           release {
               isMinifyEnabled = true
               isShrinkResources = true
               proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
               if (System.getenv("KEYSTORE_PATH") != null) {
                   signingConfig = signingConfigs.getByName("release")
               }
           }
       }
   }
   ```
   Keep ProGuard/R8 rules for Media3, Room, Hilt as their docs require; test the release build on a device before tagging.

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
