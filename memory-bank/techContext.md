# Tech Context

## Technologies
- **React Native**: Version 0.81.5 (New Architecture enabled).
- **React Native Web**: Version 0.21.2 (Web support).
- **Android**:
    -   Language: Kotlin (and Java for generated code).
    -   Native Build: CMake / NDK.
    -   Build System: Gradle.
- **iOS**:
    -   Language: Swift / Objective-C.
    -   Dependency Manager: CocoaPods.
- **Web**:
    -   Bundler: Webpack 5.
    -   Transpiler: Babel (babel-loader).

## Dependencies
- `react-native`: The core framework.
- `react-native-web`: Web compatibility layer.
- `react-native-safe-area-context`: Handling safe areas (Autolinked TurboModule).
- `react-native-lib-demo`: Local demo library (Autolinked TurboModule).
- `react-native-gradle-plugin`: Plugin for building RN apps/libraries (Patched for library mode).
- `webpack`, `webpack-cli`, `babel-loader`: Web build tools.

## Development Setup
- **Codegen**: Used to generate native interfaces from TypeScript specs.
- **CMake**: Used to compile the C++ side of the New Architecture modules.
- **Autolinking**: Automates the linking of native dependencies.
