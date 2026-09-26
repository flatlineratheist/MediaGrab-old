<div align="center">

<img width="128" src="branding/mediagrab-icon.svg" alt="MediaGrab icon">

# MediaGrab

**by [Clinerds](https://clinerds.com)**

Video and audio downloader for Android, powered by [yt-dlp](https://github.com/yt-dlp/yt-dlp).

[![Release](https://img.shields.io/github/v/release/flatlineratheist/MediaGrab?include_prereleases&label=Release&logo=github)](https://github.com/flatlineratheist/MediaGrab/releases)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-blue)](LICENSE)

</div>

## Features

- Download video and audio from any site [yt-dlp supports](https://github.com/yt-dlp/yt-dlp/blob/master/supportedsites.md).
- Embed metadata, thumbnails and subtitles.
- Download whole playlists in one go.
- Use the bundled [aria2c](https://github.com/aria2/aria2) as an external downloader.
- Run your own yt-dlp commands through saved templates.
- Material Design 3 UI with dynamic colour, written in Kotlin and Jetpack Compose.

## Download

Get the latest APK from [Releases](https://github.com/flatlineratheist/MediaGrab/releases).
Most phones need the **arm64-v8a** build.

## Build from source

Requires JDK 21 and the Android SDK (API 35).

```bash
./gradlew assembleGenericDebug      # debug APK in app/build/outputs/apk/generic/debug/
```

If Gradle can't find JDK 21, point it at Android Studio's bundled JDK:

```bash
JAVA_HOME=~/android-studio/jbr ./gradlew assembleGenericDebug
```

## Credits

MediaGrab is a modified version of an open-source app by
[JunkFood02](https://github.com/JunkFood02), released under GPL-3.0.
It is built on [yt-dlp](https://github.com/yt-dlp/yt-dlp) and
[youtubedl-android](https://github.com/yausername/youtubedl-android).
Some UI code comes from [Read You](https://github.com/Ashinch/ReadYou) and
[Music You](https://github.com/Kyant0/MusicYou), plus
[Material color utilities](https://github.com/material-foundation/material-color-utilities)
and [Monet](https://github.com/Kyant0/Monet).

## License

[GPL-3.0](LICENSE). Modified by Clinerds, 2026.
