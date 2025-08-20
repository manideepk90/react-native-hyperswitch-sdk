package com.hyperswitchsdk

import android.app.Application
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage

class HyperswitchHost(application: Application) : ReactNativeHost(application) {

  override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

  override fun getPackages(): MutableList<ReactPackage> =
    mutableListOf(
      MainReactPackage(),
      HyperswitchSdkPackage()
    )

  override fun getJSBundleFile(): String = "assets://hyperswitch.bundle"
  override fun getJSMainModuleName(): String = "index"
  fun onCreate(){

  }
}
