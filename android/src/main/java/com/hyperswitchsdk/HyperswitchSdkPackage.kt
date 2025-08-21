package com.hyperswitchsdk

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.shell.MainReactPackage
import com.facebook.react.uimanager.ViewManager // Import ViewManager

class HyperswitchSdkPackage : BaseReactPackage() {
  override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
    return when (name) {
      HyperswitchSdkModule.NAME -> HyperswitchSdkModule(reactContext)
      HyperModulesModule.NAME -> HyperModulesModule(reactContext)
      else -> null
    }
  }

  override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
    return ReactModuleInfoProvider {
      val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()
      moduleInfos[HyperswitchSdkModule.NAME] = ReactModuleInfo(
        HyperswitchSdkModule.NAME,
        HyperswitchSdkModule.NAME,
        false,  // canOverrideExistingModule
        false,  // needsEagerInit
        false,  // isCxxModule
        true // isTurboModule
      )
      moduleInfos[HyperModulesModule.NAME] = ReactModuleInfo(
        HyperModulesModule.NAME,
        HyperModulesModule.NAME,
        false,  // canOverrideExistingModule
        false,  // needsEagerInit
        false,  // isCxxModule
        true // isTurboModule
      )
      moduleInfos
    }
  }

  // Add the createViewManagers method
  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    // Replace HyperswitchViewManager() with your actual ViewManager instance(s)
    // If you have multiple ViewManagers, add them to this list.
    // For example: return listOf(HyperswitchViewManager(), AnotherViewManager())
    // If you don't have a HyperswitchViewManager yet, you'll need to create one.
    // If you don't have any ViewManagers currently, return an empty list:
    return emptyList<ViewManager<*, *>>()
  }
}
