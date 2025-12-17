# Active Context

## Current Work Focus
The primary focus has been on configuring the Android build system for the `RNHyperSwitch` library (`android/app`) to correctly support the React Native New Architecture (TurboModules) and Codegen.

## Recent Changes
- **Implemented DefaultHardwareBackBtnHandler**: Updated `demo-app`'s `MainActivity` to implement the back button handler required by React Native.
- **Fixed `libappmodules.so` Missing Issue**:
    - Created `android/app/src/main/jni/OnLoad.cpp` as a C++ entry point.
    - Created `android/app/src/main/jni/CMakeLists.txt` which includes `ReactNative-application.cmake` to handle linking of React Native core, Autolinking, and Codegen artifacts.
    - Updated `android/app/build.gradle` to set `jsRootDir` (pointing to root) and enable `externalNativeBuild` using the created CMake file.
- **Fixed Dependencies**: Changed `react-android` dependency from `implementation` to `api` in the library's `build.gradle` so consumers (`demo-app`) can access RN types.

## Next Steps
- **Verify Build**: Ensure that `demo-app` can successfully build and run, and that `libappmodules.so` is present and loads correctly.
- **Test TurboModules**: Verify that `react-native-lib-demo` and `react-native-safe-area-context` are functioning as expected within the app.
- **iOS Setup**: Check if similar configurations are needed for the iOS side (Podfile, etc.).

## Active Decisions
- **Library Structure**: The `android/app` module acts as the Android library implementation for `RNHyperSwitch`. Despite the name `app`, it is configured as a library (`com.android.library`).
- **CMake Strategy**: Utilizing `ReactNative-application.cmake` is the standard way to ensure all New Architecture components (Autolinking, Fabric, TurboModules) are linked correctly.
