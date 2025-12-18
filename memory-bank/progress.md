# Progress

## Status
- [x] Initial Project Setup
- [x] Create `DefaultHardwareBackBtnHandler` in `MainActivity`.
- [x] Fix dependencies for `demo-app` to access RN types (API dependency).
- [x] Create C++ entry point (`OnLoad.cpp`) for SDK library.
- [x] Create `CMakeLists.txt` for SDK library to link RN core, Autolinking, and Codegen.
- [x] Configure `build.gradle` for SDK library to enable CMake and set correct `jsRootDir`.
- [x] Fix soloader and TurboModules loading issues (Fixed via `@react-native+gradle-plugin+0.81.5.patch`).
- [x] Add React Native Web support (Implemented via Webpack).
- [ ] Verify `demo-app` builds successfully.
- [ ] Verify `libappmodules.so` is generated and loaded (Pending final verification).
- [ ] Verify TurboModules (`safearea`, `libdemo`) are working (Pending final verification).

## Known Issues
- `libappmodules.so` was missing (Fixed by adding CMake setup and patching gradle plugin).
- TurboModules were not adding properly (Fixed by including `ReactNative-application.cmake` and patching gradle plugin).
