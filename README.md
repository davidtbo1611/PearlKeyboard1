# Pearl Keyboard (Android)

An offline, iOS-inspired Android input method starter project. It includes a rounded QWERTY layout, emoji shortcuts/picker, basic symbol layout, light/dark theme preference, and an Android IME service.

## Build an installable APK

This environment does not include the Android SDK, so it cannot directly compile the APK. The included GitHub Actions workflow builds it for you:

1. Create a free GitHub account (if needed) and a new repository named `PearlKeyboard`.
2. Upload the contents of this folder to that repository (keep `.github/workflows/build-apk.yml`).
3. Open the repository's **Actions** tab and run **Build Pearl Keyboard APK** (or push a commit to start it).
4. Open the completed workflow run, scroll to **Artifacts**, and download `pearl-keyboard-debug-apk`.
5. Extract `app-debug.apk` on your Android phone and install it. Android may ask you to allow installation from that source.
6. Open Pearl Keyboard, tap **Enable Pearl Keyboard**, enable it in Android settings, then return and tap **Choose Pearl Keyboard**.

## Privacy

The app requests no INTERNET permission and does not send typed text anywhere. Like all third-party keyboards, it can process text entered into fields where Android permits an input method. Avoid enabling any keyboard you do not trust.

## Notes

The typeface uses Android's built-in sans-serif family as an iOS-like visual approximation. Emoji glyphs come from the Android system font; a keyboard app cannot force Apple's proprietary emoji font to appear in every other app.
