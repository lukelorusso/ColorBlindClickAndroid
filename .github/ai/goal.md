# Product Vision & Goals

## Project Overview
- **App Name**: ColorBlindClick
- **Description**: ColorBlindClick is helpful to distinguish colors. It's very simple to use, just point an object and click to get the name, RGB and hash of the color 🎨
- **Target Audience**: the app is designed to help all colorblind people

## Core Value Propositions
1. **Privacy-First**: No external cloud tracking or monetization of user data: user data collection is done
   only for business purposes.
2. **Online**: Most of the app's feature happens thanks to a remote web service which decode a color hex into a detailed description
3. **Share**: The color results are accessible via the history, were the user can select a color to preview and share it with other users

## Key Features
1. **Point & Click Color Capture**: Using the device camera (Jetpack CameraX `PreviewView`), the user aims at any real-world object and taps to capture the color of the targeted pixel.
2. **Color Decoding**: The app extracts the tapped pixel's HEX value and sends it to a remote color API to resolve a human-readable name, RGB values, and hex code — the app itself holds no built-in color database. Two providers are supported depending on locale/language:
   - English: [TheColorAPI](https://www.thecolorapi.com/)
   - Italian/French: [SaveDev](https://savedev.altervista.org/SD-Frontend/colorblindclick/index.php)
3. **Image Picker**: Users can alternatively pick an existing photo from the gallery instead of using the live camera.
4. **History**: Every decoded color is persisted (Room DB) so users can revisit past results, preview them, or delete them individually or all at once.
5. **Share**: A previewed color result can be shared outside the app (e.g. to messaging apps) so colorblind users can communicate a color to others.
6. **Settings**: Users can configure camera behavior (e.g. pixel neighbourhood sampling, save-camera options, last lens position, last zoom value) to tune capture accuracy and convenience.
7. **Localization**: The app UI and color descriptions support English, Italian, and French.
8. **Companion iOS App**: An iOS version of ColorBlindClick exists and should be considered for feature parity when relevant.

## Non-Goals
- The app does not ship its own offline color database or perform on-device color naming — decoding always relies on the remote APIs above.
- The app is not a general-purpose camera or photo-editing tool; camera/gallery access exists solely to sample a single pixel's color.
