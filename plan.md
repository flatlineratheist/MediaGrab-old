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
- Nothing is committed or pushed. GitHub still shows the old code until you push.
- The local folder under `~/Desktop/ai-code/` still has the upstream name.
- Git history still contains the old name. That's normal for a fork; only a history rewrite would remove it.

## Next steps, in order

1. **Commit and push the rebrand** to `main` (or a `rebrand` branch plus a PR).
2. **Rename the local folder** under `~/Desktop/ai-code/` to `MediaGrab`.
3. **Release signing**
   - Generate a release keystore and keep it out of git (`keystore.properties` is already in `.gitignore`).
   - Add GitHub secrets `SIGNING_KEY`, `ALIAS`, `KEY_STORE_PASSWORD`, `KEY_PASSWORD` for `.github/workflows/android.yml`.
   - Pin `ilharp/sign-android-release@nightly` in `android.yml` to a commit SHA. `@nightly` is a moving branch that receives your signing key.
4. **Decide the version line.** The build is still `2.0.0-alpha.5` (in `buildSrc/src/main/kotlin/Version.kt` and `versionCode` in `app/build.gradle.kts`, which must match).
   Because the app ID is new, you can restart at `1.0.0`.
5. **First GitHub release.** The in-app updater (`util/UpdateUtil.kt`) expects:
   - a release name like `v1.0.0` or `v1.0.0-beta.1`
   - APK asset names that contain the ABI (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`)
6. **Security fixes carried over from the original code** (found in the 2026-09-26 audit):
   - [ ] `MainActivity` / `QuickDownloadActivity` pass `ACTION_VIEW` `intent.dataString` to yt-dlp unchecked, and the yt-dlp wrapper adds no `--` before URLs.
     A "URL" starting with `-` (for example `--exec=…`) becomes a yt-dlp option. Fix: accept only strings starting with `http://` or `https://`.
   - [ ] `allowBackup="true"` with no backup rules. Saved site cookies (Room DB, `app_webview/`) go into Google Drive backups.
     Add `dataExtractionRules` / `fullBackupContent` excluding `databases/`, `app_webview/` and `files/mmkv/`.
   - [ ] Release builds keep `Log.d`, which logs full yt-dlp commands and output (`util/DownloadUtil.kt`, `Downloader.kt`).
     Add `-assumenosideeffects class android.util.Log { *; }` to `app/proguard-rules.pro`.
   - [ ] Imported command templates run as yt-dlp config files, so they can include `--exec`.
     Show a warning when an imported template contains `--exec`.
7. **Translations.** "MediaGrab by Clinerds" on the About page is hard-coded English. The other 80 locales were updated by find-and-replace, so a native speaker should skim them.
8. **yt-dlp fork.** The app doesn't use `flatlineratheist/yt-dlp` yet. yt-dlp updates come from the official `yt-dlp/yt-dlp` releases, which are hard-coded in the youtubedl-android library.
   To use the fork, publish releases in it and change the updater, or keep the fork only for patches and sync it now and then.
9. **Store listing.** The fastlane metadata was deleted. Write new Play Store / F-Droid text and screenshots when you publish.

## Rules to keep

- **App IDs:** every Clinerds Android app gets its own ID under `com.clinerds.<appname>`. This one is `com.clinerds.mediagrab`.
  Never reuse an ID: Play Store and Android treat it as the app's permanent identity.

- **GPL-3.0:** keep `LICENSE`, publish the source for every APK you distribute, and keep the credit and modification note in `README.md`.
- **Naming:** the original project forbids forks from using its name. Keep that name out of the code, strings and assets.
  Check before each release with:
  `grep -rniI "s[e]al" --exclude-dir={.git,build,.gradle,.kotlin} . | grep -vw sealed` (it should print nothing; the pattern is written `s[e]al` so this file doesn't match itself).
- **Icon changes:** edit `branding/mediagrab-icon.svg`, re-render with headless Chrome, then regenerate the `mipmap-*` PNGs.
  The adaptive foreground uses 66% of 108dp; legacy icons use 94% of 48dp.
