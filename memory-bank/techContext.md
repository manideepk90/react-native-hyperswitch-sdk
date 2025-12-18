# Tech Context

## Technologies
- **React Native**: Version 0.81.5 (New Architecture enabled).
- **Android**:
    -   Language: Kotlin (and Java for generated code).
    -   Native Build: CMake / NDK.
    -   Build System: Gradle.
- **iOS**:
    -   Language: Swift / Objective-C.
    -   Dependency Manager: CocoaPods.

## Dependencies
- `react-native`: The core framework.
- `react-native-safe-area-context`: Handling safe areas (Autolinked TurboModule).
- `react-native-lib-demo`: Local demo library (Autolinked TurboModule).
- `react-native-gradle-plugin`: Plugin for building RN apps/libraries.

## Development Setup
- **Codegen**: Used to generate native interfaces from TypeScript specs.
- **CMake**: Used to compile the C++ side of the New Architecture modules.
- **Autolinking**: Automates the linking of native dependencies.
