//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.hyperswitchsdk.react

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import com.facebook.infer.annotation.Assertions
import com.facebook.react.ReactHost
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.devsupport.DoubleTapReloadRecognizer
import com.facebook.react.devsupport.ReleaseDevSupportManager
import com.facebook.react.devsupport.interfaces.DevSupportManager
import com.facebook.react.interfaces.fabric.ReactSurface
import com.facebook.react.internal.featureflags.ReactNativeFeatureFlags.enableBridgelessArchitecture
import com.facebook.react.internal.featureflags.ReactNativeFeatureFlags.enableFabricRenderer
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler

class ReactDelegate {
  private val mActivity: Activity
  private var mReactRootView: ReactRootView? = null
  private val mMainComponentName: String
  private val mLaunchOptions: Bundle?
  private val mDoubleTapReloadRecognizer: DoubleTapReloadRecognizer?
  private var reactNativeHost: ReactNativeHost? = null
  var reactHost: ReactHost? = null
    private set
  private var mReactSurface: ReactSurface? = null
  protected var isFabricEnabled: Boolean = enableFabricRenderer()
    private set

  @Deprecated("")
  constructor(
    activity: Activity,
    reactNativeHost: ReactNativeHost?,
    appKey: String,
    launchOptions: Bundle?
  ) {
    this.mActivity = activity
    this.mMainComponentName = appKey
    this.mLaunchOptions = launchOptions
    this.mDoubleTapReloadRecognizer = DoubleTapReloadRecognizer()
    this.reactNativeHost = reactNativeHost
  }

  constructor(activity: Activity, reactHost: ReactHost?, appKey: String, launchOptions: Bundle?) {
    this.mActivity = activity
    this.mMainComponentName = appKey
    this.mLaunchOptions = launchOptions
    this.mDoubleTapReloadRecognizer = DoubleTapReloadRecognizer()
    this.reactHost = reactHost
  }

  constructor(
    activity: Activity,
    reactNativeHost: ReactNativeHost?,
    appKey: String,
    launchOptions: Bundle?,
    fabricEnabled: Boolean
  ) {
    this.isFabricEnabled = fabricEnabled
    this.mActivity = activity
    this.mMainComponentName = appKey
    this.mLaunchOptions = launchOptions
    this.mDoubleTapReloadRecognizer = DoubleTapReloadRecognizer()
    this.reactNativeHost = reactNativeHost
  }

  private val devSupportManager: DevSupportManager?
    get() {
      if (enableBridgelessArchitecture() && this.reactHost != null && this.reactHost!!.devSupportManager != null) {
        return this.reactHost!!.devSupportManager
      } else {
        return if (this.reactNativeHost!!.hasInstance() && this.reactNativeHost!!.getReactInstanceManager() != null) this.reactNativeHost!!.getReactInstanceManager()
          .getDevSupportManager() else null
      }
    }

  fun onHostResume() {
    if (this.mActivity !is DefaultHardwareBackBtnHandler) {
      throw ClassCastException("Host Activity does not implement DefaultHardwareBackBtnHandler")
    } else {
      if (enableBridgelessArchitecture()) {
        this.reactHost!!.onHostResume(
          this.mActivity,
          this.mActivity as DefaultHardwareBackBtnHandler
        )
      } else if (this.reactNativeHost!!.hasInstance()) {
        this.reactNativeHost!!.getReactInstanceManager()
          .onHostResume(this.mActivity, this.mActivity as DefaultHardwareBackBtnHandler)
      }
    }
  }

  fun onUserLeaveHint() {
    if (enableBridgelessArchitecture()) {
      this.reactHost!!.onHostLeaveHint(this.mActivity)
    } else if (this.reactNativeHost!!.hasInstance()) {
      this.reactNativeHost!!.getReactInstanceManager().onUserLeaveHint(this.mActivity)
    }
  }

  fun onHostPause() {
    if (enableBridgelessArchitecture()) {
      this.reactHost!!.onHostPause(this.mActivity)
    } else if (this.reactNativeHost!!.hasInstance()) {
      this.reactNativeHost!!.getReactInstanceManager().onHostPause(this.mActivity)
    }
  }

  fun onHostDestroy() {
    this.unloadApp()
    if (enableBridgelessArchitecture()) {
      this.reactHost!!.onHostDestroy(this.mActivity)
    } else if (this.reactNativeHost!!.hasInstance()) {
      this.reactNativeHost!!.getReactInstanceManager().onHostDestroy(this.mActivity)
    }
  }

  fun onBackPressed(): Boolean {
    if (enableBridgelessArchitecture()) {
      this.reactHost!!.onBackPressed()
      return true
    } else if (this.reactNativeHost!!.hasInstance()) {
      this.reactNativeHost!!.getReactInstanceManager().onBackPressed()
      return true
    } else {
      return false
    }
  }

  fun onNewIntent(intent: Intent): Boolean {
    if (enableBridgelessArchitecture()) {
      this.reactHost!!.onNewIntent(intent)
      return true
    } else if (this.reactNativeHost!!.hasInstance()) {
      this.reactNativeHost!!.getReactInstanceManager().onNewIntent(intent)
      return true
    } else {
      return false
    }
  }

  fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?,
    shouldForwardToReactInstance: Boolean
  ) {
    if (enableBridgelessArchitecture()) {
      this.reactHost!!.onActivityResult(this.mActivity, requestCode, resultCode, data)
    } else if (this.reactNativeHost!!.hasInstance() && shouldForwardToReactInstance) {
      this.reactNativeHost!!.getReactInstanceManager()
        .onActivityResult(this.mActivity, requestCode, resultCode, data)
    }
  }

  fun onWindowFocusChanged(hasFocus: Boolean) {
    if (enableBridgelessArchitecture()) {
      this.reactHost!!.onWindowFocusChange(hasFocus)
    } else if (this.reactNativeHost!!.hasInstance()) {
      this.reactNativeHost!!.getReactInstanceManager().onWindowFocusChange(hasFocus)
    }
  }

  fun onConfigurationChanged(newConfig: Configuration?) {
    if (enableBridgelessArchitecture()) {
      this.reactHost!!.onConfigurationChanged(
        (Assertions.assertNotNull<Activity?>(
          this.mActivity
        ) as Context?)!!
      )
    } else if (this.reactNativeHost!!.hasInstance()) {
      this.reactInstanceManager!!.onConfigurationChanged(
        Assertions.assertNotNull<Activity?>(this.mActivity) as Context?,
        newConfig
      )
    }
  }

  fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
    if (keyCode != 90 || (!enableBridgelessArchitecture() || this.reactHost == null || this.reactHost!!.devSupportManager == null) && (!this.reactNativeHost!!.hasInstance() || !this.reactNativeHost!!.getUseDeveloperSupport())) {
      return false
    } else {
      event.startTracking()
      return true
    }
  }

  fun onKeyLongPress(keyCode: Int): Boolean {
    if (keyCode == 90) {
      if (enableBridgelessArchitecture() && this.reactHost != null) {
        val devSupportManager = this.reactHost!!.devSupportManager
        if (devSupportManager != null && devSupportManager !is ReleaseDevSupportManager) {
          devSupportManager.showDevOptionsDialog()
          return true
        }
      } else if (this.reactNativeHost!!.hasInstance() && this.reactNativeHost!!.getUseDeveloperSupport()) {
        this.reactNativeHost!!.getReactInstanceManager().showDevOptionsDialog()
        return true
      }
    }

    return false
  }

  fun reload() {
//    val devSupportManager = this.devSupportManager
//    if (devSupportManager != null) {
//      if (devSupportManager is ReleaseDevSupportManager) {
//        if (enableBridgelessArchitecture()) {
//          if (this.reactHost != null) {
//            this.reactHost!!.reload("ReactDelegate.reload()")
//          }
//        } else {
//          UiThreadUtil.runOnUiThread(Runnable {
//            if (this.reactNativeHost!!.hasInstance() && this.reactNativeHost!!.getReactInstanceManager() != null) {
//              this.reactNativeHost!!.getReactInstanceManager().recreateReactContextInBackground()
//            }
//          })
//        }
//      } else {
//        devSupportManager.handleReloadJS()
//      }
//    }
  }

  @JvmOverloads
  fun loadApp(appKey: String = this.mMainComponentName) {
    if (enableBridgelessArchitecture()) {
      if (this.mReactSurface == null) {
        this.mReactSurface =
          this.reactHost!!.createSurface(this.mActivity, appKey, this.mLaunchOptions)
      }

      this.mReactSurface!!.start()
    } else {
      check(this.mReactRootView == null) { "Cannot loadApp while app is already running." }

      this.mReactRootView = this.createRootView()
      this.mReactRootView!!.startReactApplication(
        this.reactNativeHost!!.getReactInstanceManager(),
        appKey,
        this.mLaunchOptions
      )
    }
  }

  fun unloadApp() {
    if (enableBridgelessArchitecture()) {
      if (this.mReactSurface != null) {
        this.mReactSurface!!.stop()
        this.mReactSurface = null
      }
    } else if (this.mReactRootView != null) {
      this.mReactRootView!!.unmountReactApplication()
      this.mReactRootView = null
    }
  }

  fun setReactSurface(reactSurface: ReactSurface?) {
    this.mReactSurface = reactSurface
  }

  var reactRootView: ReactRootView?
    get() {
      if (enableBridgelessArchitecture()) {
        return if (this.mReactSurface != null) this.mReactSurface!!.view as ReactRootView? else null
      } else {
        return this.mReactRootView
      }
    }
    set(reactRootView) {
      this.mReactRootView = reactRootView
    }

  protected fun createRootView(): ReactRootView {
    val reactRootView = ReactRootView(this.mActivity)
    reactRootView.setIsFabric(this.isFabricEnabled)
    return reactRootView
  }

  fun shouldShowDevMenuOrReload(keyCode: Int, event: KeyEvent?): Boolean {
    val devSupportManager = this.devSupportManager
    if (devSupportManager != null && devSupportManager !is ReleaseDevSupportManager) {
      if (keyCode == 82) {
        devSupportManager.showDevOptionsDialog()
        return true
      } else {
        val didDoubleTapR =
          (Assertions.assertNotNull<DoubleTapReloadRecognizer?>(this.mDoubleTapReloadRecognizer) as DoubleTapReloadRecognizer).didDoubleTapR(
            keyCode,
            this.mActivity.getCurrentFocus()
          )
        if (didDoubleTapR) {
          devSupportManager.handleReloadJS()
          return true
        } else {
          return false
        }
      }
    } else {
      return false
    }
  }

  val reactInstanceManager: ReactInstanceManager?
    get() = this.reactNativeHost!!.getReactInstanceManager()

  val currentReactContext: ReactContext?
    get() {
      if (enableBridgelessArchitecture()) {
        return if (this.reactHost != null) this.reactHost!!.currentReactContext else null
      } else {
        return this.reactInstanceManager!!.getCurrentReactContext()
      }
    }
}
