let sendMessageToNative = str => {
   TurboModulesHyper.sendMessageToNative(str)
}

let useExitPaymentsheet = () => {
  TurboModulesHyper.useExitPaymentsheet()
}

let useExitCard = () => {
  TurboModulesHyper.useExitCard()
}

let useExitWidget = () => {
  TurboModulesHyper.useExitWidget()
}

let launchApplePay = (requestObj: string, callback) => {
 TurboModulesHyper.launchApplePay(requestObj, callback)
}

let launchGPay = (requestObj: string, callback) => {
   TurboModulesHyper.launchGPay(requestObj, callback)
}

let launchWidgetPaymentSheet = (requestObj: string, callback) => {
     TurboModulesHyper.launchWidgetPaymentSheet(requestObj, callback)

}

let updateWidgetHeight = {
  TurboModulesHyper.updateWidgetHeight
}

// Export the hyperModule object
let hyperModule = {
  TurboModulesHyper.hyperTurboModule
}

// Export stringifiedResStatus function
let stringifiedResStatus = {
   TurboModulesHyper.stringifiedResStatus
}
