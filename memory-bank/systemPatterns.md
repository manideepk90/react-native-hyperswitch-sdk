# System Patterns

## Architecture
The project follows a Monorepo-like structure where the SDK library and the demo application coexist.

### Modules
- **Root (`hyperSwitch`)**: Contains `package.json`, `node_modules`, and project-wide configuration.
- **Library (`android/app`)**: The Android implementation of the SDK. It wraps the native HyperSwitch functionality and exposes it to React Native. It is configured as an Android Library (`com.android.library`).
- **Consumer (`android/demo-app`)**: A standard Android Application (`com.android.application`) that depends on the Library module. It serves as a testbed and usage example.
- **Dependencies (`react-native-lib-demo`)**: A local library module demonstrating TurboModule integration.

## Key Technical Decisions
- **React Native New Architecture**: The project fully embraces the New Architecture (TurboModules, Fabric) for performance and native interoperability.
- **Native-as-Library**: The SDK is built as a native library that embeds the React Native runtime (or links against it) and can be consumed by other native apps or RN apps.
- **Gradle Plugin Patching**: To support the "Native-as-Library" pattern, the `@react-native/gradle-plugin` is patched to handle `generatePackageList=true`, allowing the SDK to build as a library with correct shared library packaging (`pickFirst` for JNI libs).
- **Codegen**: Heavily relies on React Native Codegen to generate type-safe bindings between JavaScript and Native code.
- **Autolinking**: Uses standard React Native Autolinking to manage native dependencies.

## Component Relationships
- `demo-app` -> depends on -> `android/app` (SDK Library).
- `android/app` -> depends on -> `react-native` (and other RN libraries).
- `android/app` -> links -> `react-native-lib-demo` (via Autolinking/CMake).
