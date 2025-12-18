# Active Context

## Current Focus
- Configuring Re.pack (Rspack) for asset handling and resolution
- Supporting library-as-optional mode for React Native Hyperswitch SDK
- Validating the patch to @react-native/gradle-plugin which fixes soloader and turbomodules issues
- Android build system configuration for New Architecture (TurboModules)

## Key Implementation Details
- **MoveAssetsPlugin**:
  - A custom Rspack plugin (`plugins/MoveAssetsPlugin.mjs`) moves non-optional assets (like the main bundle) to `appAssetsPath` during production builds.
  - This ensures that the main bundle is available in the app's assets folder (`android/app/src/main/assets`) for offline usage.
  - Configured conditionally in `rspack.config.mjs` to run only in production mode (`mode !== 'development'`) to avoid interfering with the dev server.
- **Rspack Resolution**:
  - `Repack.getResolveOptions` must be called with `platform` argument to ensure correct file resolution (e.g., loading `.android.js` instead of `.ios.js`).
  - This fixes issues like `SettingsManager` (iOS only) being loaded on Android.
- **Library Mode (`generatePackageList=true`)**:
  - The SDK builds as a library instead of an application.
  - ReactPlugin.kt conditionally configures Android components based on this flag.
  - This mode requires specific handling of shared libraries (.so files) to avoid conflicts and ensure proper loading.
- **Patch Fixes (`@react-native+gradle-plugin+0.81.5.patch`)**:
  - Fixes soloader and TurboModules issues by configuring `pickFirst` packaging options for JNI libs (`libfbjni.so`, `libc++_shared.so`, `libjsi.so`, `libreactnative.so`).
  - Modifies `ReactPlugin.kt`, `AgpConfiguratorUtils.kt`, and `NdkConfiguratorUtils.kt` to support `LibraryAndroidComponentsExtension`.

## Recent Changes
- Created `plugins/MoveAssetsPlugin.mjs` to handle asset moving.
- Updated `rspack.config.mjs` to use `MoveAssetsPlugin` and fix resolution options.
- Commit `7adb77c`: part4: library as optional
- Commit `d74b40b`: chore: changed paths
- Commit `d576544`: part4: android changes
- **Patch Applied**: `@react-native+gradle-plugin+0.81.5.patch` - Fixes soloader and TurboModules loading issues in library mode.

## Current Status
- Rspack configuration updated and verified.
- `hyperswitch.bundle` is correctly moved to assets folder in production builds.
- Dev server (`yarn start`) works correctly (plugin disabled).
- Android build system configured for New Architecture.

## Next Steps
1. Verify iOS compatibility for library mode.
2. Update documentation for library usage.
3. Address the typo in the patch (`generatePacakageList`) if it causes issues.

## Critical Patterns
- **Conditional Plugin Configuration**: The gradle plugin behaves differently based on `generatePackageList` property. Rspack plugins also need conditional application based on `mode`.
- **Packaging Options**: Using `pickFirst` for JNI libs is critical for avoiding "more than one file was found" errors.
