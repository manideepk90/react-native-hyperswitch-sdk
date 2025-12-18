# Active Context

## Current Focus
- Supporting library-as-optional mode for React Native Hyperswitch SDK
- Validating the patch to @react-native/gradle-plugin which fixes soloader and turbomodules issues
- Android build system configuration for New Architecture (TurboModules)

## Key Implementation Details
- **Library Mode (`generatePackageList=true`)**:
  - The SDK builds as a library instead of an application.
  - ReactPlugin.kt conditionally configures Android components based on this flag.
  - This mode requires specific handling of shared libraries (.so files) to avoid conflicts and ensure proper loading.
- **Patch Fixes (`@react-native+gradle-plugin+0.81.5.patch`)**:
  - Fixes soloader and TurboModules issues by configuring `pickFirst` packaging options for JNI libs (`libfbjni.so`, `libc++_shared.so`, `libjsi.so`, `libreactnative.so`).
  - Modifies `ReactPlugin.kt`, `AgpConfiguratorUtils.kt`, and `NdkConfiguratorUtils.kt` to support `LibraryAndroidComponentsExtension`.
- **Typo Note**: The patch contains a typo `generatePacakageList` in `AgpConfiguratorUtils.kt` which should be monitored, though the user reports the fix is working.

## Recent Changes
- Commit `7adb77c`: part4: library as optional
- Commit `d74b40b`: chore: changed paths
- Commit `d576544`: part4: android changes
- **Patch Applied**: `@react-native+gradle-plugin+0.81.5.patch` - Fixes soloader and TurboModules loading issues in library mode.

## Current Status
- Library mode implementation is active.
- Patch for gradle plugin is applied and reported to fix key issues.
- Android build system configured for New Architecture.

## Next Steps
1. Verify the fix by running the build/app (user confirmed fix, but verification is good practice).
2. Update documentation for library usage to include the need for this patch/configuration.
3. Address the typo in the patch (`generatePacakageList`) if it causes issues in `AgpConfiguratorUtils`.
4. Verify iOS compatibility for library mode.

## Critical Patterns
- **Conditional Plugin Configuration**: The gradle plugin behaves differently based on `generatePackageList` property.
- **Packaging Options**: Using `pickFirst` for JNI libs is critical for avoiding "more than one file was found" errors and ensuring correct library loading in the library-as-module setup.
