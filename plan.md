# MediaGrab — plan

MediaGrab by Clinerds: an Android video/audio downloader built on yt-dlp.
Repo: https://github.com/flatlineratheist/MediaGrab (a GPL-3.0 fork of JunkFood02's app).

## Where things stand (2026-09-26)

**Done**
- Rebrand: app ID `com.clinerds.mediagrab`, launcher name "MediaGrab", "MediaGrab by Clinerds" at the top of the About page.
  No "seal" is left in file contents or names. The only matches are the Kotlin keyword `sealed`.
- New app icon from `branding/mediagrab-icon.svg`: adaptive, legacy, themed (monochrome) and notification icons.
- Removed upstream-only parts: Sponsors page (which shipped the upstream developer's GitHub token), Telegram/Matrix/Weblate links,
  translated READMEs, CHANGELOG, fastlane/F-Droid metadata, FUNDING, issue templates, sponsor and issue-bot workflows.
- The app updater, README and release links now point to `flatlineratheist/MediaGrab`.
- GitHub: repo renamed to `MediaGrab`, description updated, yt-dlp forked to `flatlineratheist/yt-dlp`.
- `./gradlew assembleGenericDebug` builds (needs JDK 21, see README).

**Not done yet**
- The local folder under `~/Desktop/ai-code/` still has the upstream name.
- Git history still contains the old name. That's normal for a fork; only a history rewrite would remove it.

## Next steps, in order

1. **Rename the local folder** under `~/Desktop/ai-code/` to `MediaGrab`.
2. **Release signing**
   - [x] Release key at `~/keystores/clinerds/mediagrab-release.jks` (alias `mediagrab`, RSA 4096, valid until 2054).
     The password is in `keystore.properties` in the repo root, which git ignores (as it does `*.jks`).
     **Back up both files** to a password manager or an offline copy. Without them you can never ship an update to existing installs.
   - [x] Local release builds are signed automatically when `keystore.properties` exists (`./gradlew assembleGenericRelease`).
   - [x] `.github/workflows/android.yml` signs through Gradle from the secrets. The third-party `ilharp/sign-android-release@nightly` action is gone.
   - [ ] Add the four GitHub secrets (`SIGNING_KEY`, `ALIAS`, `KEY_STORE_PASSWORD`, `KEY_PASSWORD`). Run from the repo root:
     ```bash
     cd <repo> && get(){ grep "^$1=" keystore.properties | cut -d= -f2-; }; R=flatlineratheist/MediaGrab
     base64 -w0 "$(get storeFile)" | gh secret set SIGNING_KEY -R $R
     get keyAlias | tr -d '\n' | gh secret set ALIAS -R $R
     get storePassword | tr -d '\n' | gh secret set KEY_STORE_PASSWORD -R $R
     get keyPassword | tr -d '\n' | gh secret set KEY_PASSWORD -R $R
     ```
   - [ ] Run the "Build Release APK" workflow once (`gh workflow run android.yml -R flatlineratheist/MediaGrab`) and check that it produces signed APKs.
3. **Decide the version line.** The build is still `2.0.0-alpha.5` (in `buildSrc/src/main/kotlin/Version.kt` and `versionCode` in `app/build.gradle.kts`, which must match).
   Because the app ID is new, you can restart at `1.0.0`.
4. **First GitHub release.** The in-app updater (`util/UpdateUtil.kt`) expects:
   - a release name like `v1.0.0` or `v1.0.0-beta.1`
   - APK asset names that contain the ABI (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`)
5. **Security fixes carried over from the original code** (found in the 2026-09-26 audit):
   - [ ] `MainActivity` / `QuickDownloadActivity` pass `ACTION_VIEW` `intent.dataString` to yt-dlp unchecked, and the yt-dlp wrapper adds no `--` before URLs.
     A "URL" starting with `-` (for example `--exec=…`) becomes a yt-dlp option. Fix: accept only strings starting with `http://` or `https://`.
   - [ ] `allowBackup="true"` with no backup rules. Saved site cookies (Room DB, `app_webview/`) go into Google Drive backups.
     Add `dataExtractionRules` / `fullBackupContent` excluding `databases/`, `app_webview/` and `files/mmkv/`.
   - [ ] Release builds keep `Log.d`, which logs full yt-dlp commands and output (`util/DownloadUtil.kt`, `Downloader.kt`).
     Add `-assumenosideeffects class android.util.Log { *; }` to `app/proguard-rules.pro`.
   - [ ] Imported command templates run as yt-dlp config files, so they can include `--exec`.
     Show a warning when an imported template contains `--exec`.
6. **Translations.** "MediaGrab by Clinerds" on the About page is hard-coded English. The other 80 locales were updated by find-and-replace, so a native speaker should skim them.
7. **yt-dlp fork.** The app doesn't use `flatlineratheist/yt-dlp` yet. yt-dlp updates come from the official `yt-dlp/yt-dlp` releases, which are hard-coded in the youtubedl-android library.
   To use the fork, publish releases in it and change the updater, or keep the fork only for patches and sync it now and then.
8. **Store listing.** The fastlane metadata was deleted. Write new Play Store / F-Droid text and screenshots when you publish.

## Rules to keep

- **App IDs:** every Clinerds Android app gets its own ID under `com.clinerds.<appname>`. This one is `com.clinerds.mediagrab`.
  Never reuse an ID: Play Store and Android treat it as the app's permanent identity.

- **GPL-3.0:** keep `LICENSE`, publish the source for every APK you distribute, and keep the credit and modification note in `README.md`.
- **Naming:** the original project forbids forks from using its name. Keep that name out of the code, strings and assets.
  Check before each release with:
  `grep -rniI "s[e]al" --exclude-dir={.git,build,.gradle,.kotlin} . | grep -vw sealed` (it should print nothing; the pattern is written `s[e]al` so this file doesn't match itself).
- **Icon changes:** edit `branding/mediagrab-icon.svg`, re-render with headless Chrome, then regenerate the `mipmap-*` PNGs.
  The adaptive foreground uses 66% of 108dp; legacy icons use 94% of 48dp.
