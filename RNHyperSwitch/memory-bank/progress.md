# Progress

## Status
- [x] Initial Project Setup
- [x] Create `DefaultHardwareBackBtnHandler` in `MainActivity`.
- [x] Fix dependencies for `demo-app` to access RN types (API dependency).
- [x] Create C++ entry point (`OnLoad.cpp`) for SDK library.
- [x] Create `CMakeLists.txt` for SDK library to link RN core, Autolinking, and Codegen.
- [x] Configure `build.gradle` for SDK library to enable CMake and set correct `jsRootDir`.
- [ ] Verify `demo-app` builds successfully.
- [ ] Verify `libappmodules.so` is generated and loaded.
- [ ] Verify TurboModules (`safearea`, `libdemo`) are working.

## Known Issues
- `libappmodules.so` was missing (Fixed by adding CMake setup).
- TurboModules were not adding properly (Fixed by including `ReactNative-application.cmake`).
