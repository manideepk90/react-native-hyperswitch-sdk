package com.hyperswitchsdk

import android.os.Bundle
import androidx.annotation.Nullable
import com.facebook.hermes.reactexecutor.HermesExecutorFactory
import com.facebook.react.ReactActivity
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactPackage
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.common.LifecycleState
import com.facebook.react.defaults.DefaultTurboModuleManagerDelegate
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import com.facebook.react.uimanager.ViewManager
import com.facebook.react.views.text.ReactTextViewManager
import com.facebook.react.views.view.ReactViewManager

class SecondReactActivity : ReactActivity(), DefaultHardwareBackBtnHandler {
  private lateinit var mReactRootView: ReactRootView
  private lateinit var mReactInstanceManager: ReactInstanceManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(application)
      .setCurrentActivity(this)
      .addPackage(MainReactPackage())
      .setBundleAssetName("hyperswitch.bundle")
      .setJavaScriptExecutorFactory(HermesExecutorFactory())
      .setUseDeveloperSupport(false)
      .setInitialLifecycleState(LifecycleState.BEFORE_RESUME)
      .setReactPackageTurboModuleManagerDelegateBuilder(DefaultTurboModuleManagerDelegate.Builder())
      .build()

    mReactRootView = ReactRootView(this.application)
    mReactRootView.startReactApplication(mReactInstanceManager, "hyperSwitch", null)

    setContentView(mReactRootView)
  }

  override fun onPause() {
    super.onPause()
    if (::mReactInstanceManager.isInitialized) {
      mReactInstanceManager.onHostPause(this)
    }
  }

  override fun onResume() {
    super.onResume()
    if (::mReactInstanceManager.isInitialized) {
      mReactInstanceManager.onHostResume(this, this)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    if (::mReactInstanceManager.isInitialized) {
      mReactInstanceManager.onHostDestroy(this)
    }
    if (::mReactRootView.isInitialized) {
      mReactRootView.unmountReactApplication()
    }
  }

  override fun invokeDefaultOnBackPressed() {
    onBackPressed()
  }
}
