// TurboModule interface definition using external bindings
@module("./spec/NativeHyperModule") @val external sendMessageToNativeTurbo: string => unit = "sendMessageToNative"
@module("./spec/NativeHyperModule")
external launchApplePayTurbo: (string, JSON.t => unit) => unit = "launchApplePay"
@module("./spec/NativeHyperModule") external launchGPayTurbo: (string, JSON.t => unit) => unit = "launchGPay"
@module("./spec/NativeHyperModule")
external exitPaymentsheetTurbo: (int, string, bool) => unit = "exitPaymentsheet"
@module("./spec/NativeHyperModule")
external exitPaymentMethodManagementTurbo: (int, string, bool) => unit =
  "exitPaymentMethodManagement"
@module("./spec/NativeHyperModule") external exitWidgetTurbo: (string, string) => unit = "exitWidget"
@module("./spec/NativeHyperModule") external exitCardFormTurbo: string => unit = "exitCardForm"
@module("./spec/NativeHyperModule")
external launchWidgetPaymentSheetTurbo: (string, JSON.t => unit) => unit =
  "launchWidgetPaymentSheet"
@module("./spec/NativeHyperModule") external onAddPaymentMethodTurbo: string => unit = "onAddPaymentMethod"
@module("./spec/NativeHyperModule")
external exitWidgetPaymentsheetTurbo: (int, string, bool) => unit = "exitWidgetPaymentsheet"
@module("./spec/NativeHyperModule") external updateWidgetHeightTurbo: int => unit = "updateWidgetHeight"

// TurboModule interface as a record
let turboModules = {
  "sendMessageToNative": sendMessageToNativeTurbo,
  "launchApplePay": launchApplePayTurbo,
  "launchGPay": launchGPayTurbo,
  "exitPaymentsheet": exitPaymentsheetTurbo,
  "exitPaymentMethodManagement": exitPaymentMethodManagementTurbo,
  "exitWidget": exitWidgetTurbo,
  "exitCardForm": exitCardFormTurbo,
  "launchWidgetPaymentSheet": launchWidgetPaymentSheetTurbo,
  "onAddPaymentMethod": onAddPaymentMethodTurbo,
  "exitWidgetPaymentsheet": exitWidgetPaymentsheetTurbo,
  "updateWidgetHeight": updateWidgetHeightTurbo,
}
