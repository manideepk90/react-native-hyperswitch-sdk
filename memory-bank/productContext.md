# Product Context

## Problem
Integrating native payment SDKs into cross-platform frameworks like React Native involves dealing with platform-specific native code, build configurations, and bridging logic. This often leads to complexity and maintenance overhead for developers who just want to add payments to their app.

## Solution
RNHyperSwitch abstracts the complexity of the native HyperSwitch SDKs. It exposes a clean, idiomatic JavaScript/TypeScript API while managing the native lifecycle, UI rendering (via native fragments/views), and event handling under the hood.

## User Experience Goals
- **Ease of Installation**: The SDK should be easy to install and configure (Autolinking).
- **Native Performance**: Payments UI should feel native and performant.
- **Reliability**: The SDK should handle edge cases, network issues, and payment states gracefully.
- **Developer Experience**: Clear documentation, typed APIs, and minimal boilerplate code.
