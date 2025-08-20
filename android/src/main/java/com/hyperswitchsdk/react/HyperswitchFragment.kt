package com.hyperswitchsdk.react

import android.annotation.TargetApi
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.facebook.hermes.reactexecutor.HermesExecutorFactory
import com.facebook.react.*
import com.facebook.react.ReactFragment
import com.facebook.react.common.LifecycleState
import com.facebook.react.defaults.DefaultTurboModuleManagerDelegate
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.modules.core.PermissionAwareActivity
import com.facebook.react.modules.core.PermissionListener
import com.facebook.react.shell.MainReactPackage

open class HyperswitchFragment : ReactFragment(),
  PermissionAwareActivity {
  private var reactDelegate: com.facebook.react.ReactDelegate? = null
  private var mPermissionListener: PermissionListener? = null
  private var originalSoftInputMode: Int = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
  private var mReactInstanceManager: ReactInstanceManager? = null
  private var mReactRootView: ReactRootView? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var mainComponentName: String? = null
    var launchOptions: Bundle? = null
    if (this.arguments != null) {
      mainComponentName = this.requireArguments().getString("arg_component_name")
      launchOptions = this.requireArguments().getBundle("arg_launch_options")
    }
    checkNotNull(mainComponentName) { "Cannot loadApp if component name is null" }

//    reactDelegate = ReactDelegate(
//      this.activity,
//      reactNativeHost, mainComponentName, launchOptions
//    )
    if (mReactInstanceManager == null) {
      val packages = listOf<ReactPackage>(MainReactPackage())
      mReactInstanceManager = ReactInstanceManager.builder()
        .setApplication(activity?.application)
        .setCurrentActivity(activity)
        .addPackages(packages)
        .setBundleAssetName("hyperswitch.bundle")
        .setJavaScriptExecutorFactory(HermesExecutorFactory())
        .setUseDeveloperSupport(false)
        .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
        .setReactPackageTurboModuleManagerDelegateBuilder(DefaultTurboModuleManagerDelegate.Builder())
        .build()
    }

  }


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    try {
      if (mReactRootView == null) {
        mReactRootView = ReactRootView(context)
//
//        val packages = listOf<ReactPackage>(MainReactPackage())

//        mReactInstanceManager = ReactInstanceManager.builder()
//          .setApplication(activity?.application)
//          .setCurrentActivity(activity)
//          .addPackages(packages)
//          .setBundleAssetName("hyperswitch.bundle")
//          .setJavaScriptExecutorFactory(HermesExecutorFactory())
//          .setUseDeveloperSupport(false)
//          .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
//          .setReactPackageTurboModuleManagerDelegateBuilder(DefaultTurboModuleManagerDelegate.Builder())
//          .build()

        var mainComponentName: String? = null
        var launchOptions: Bundle? = null
        if (this.arguments != null) {
          mainComponentName = this.requireArguments().getString("arg_component_name")
          launchOptions = this.requireArguments().getBundle("arg_launch_options")
        }
        checkNotNull(mainComponentName) { "Cannot loadApp if component name is null" }

        mReactRootView?.startReactApplication(mReactInstanceManager!!, mainComponentName, launchOptions)
      }

      originalSoftInputMode = activity?.window?.attributes?.softInputMode ?: WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
      setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

      return mReactRootView
    } catch (e: Exception) {
      e.printStackTrace()
      // Return empty view if RN instance fails to start
      return View(context)
    }
  }

  private fun setSoftInputMode(inputMode: Int) {
    if(originalSoftInputMode != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
      activity?.window?.setSoftInputMode(inputMode)
  }

  override fun onResume() {
    super.onResume()
    mReactInstanceManager?.onHostResume(activity, activity as DefaultHardwareBackBtnHandler?)
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
  }

  override fun onPause() {
    super.onPause()
    mReactInstanceManager?.onHostPause()
    setSoftInputMode(originalSoftInputMode)
  }

  override fun onDestroy() {
    super.onDestroy()
    mReactInstanceManager?.onHostDestroy()
    mReactRootView?.unmountReactApplication()
    mReactInstanceManager = null
    mReactRootView = null
    setSoftInputMode(originalSoftInputMode)
  }

  override fun onHiddenChanged(hidden: Boolean) {
    super.onHiddenChanged(hidden)

    if (hidden) {
      setSoftInputMode(originalSoftInputMode)
    } else {
      setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    mReactInstanceManager?.onActivityResult(activity, requestCode, resultCode, data)
  }

  override fun onBackPressed(): Boolean {
    return (mReactInstanceManager?.onBackPressed() ?: false) as Boolean
  }

  override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
    // Since dev support is disabled, no need to handle dev menu
    return false
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (mPermissionListener != null && mPermissionListener!!.onRequestPermissionsResult(
        requestCode,
        permissions,
        grantResults
      )
    ) {
      mPermissionListener = null
    }
  }

  override fun checkPermission(permission: String, pid: Int, uid: Int): Int {
    return this.requireActivity().checkPermission(permission, pid, uid)
  }

  @TargetApi(23)
  override fun checkSelfPermission(permission: String): Int {
    return this.requireActivity().checkSelfPermission(permission)
  }

  @TargetApi(23)
  override fun requestPermissions(
    permissions: Array<String>,
    requestCode: Int,
    listener: PermissionListener?
  ) {
    mPermissionListener = listener
    this.requestPermissions(permissions, requestCode)
  }

  class Builder {
    private lateinit var mComponentName: String
    private lateinit var mLaunchOptions: Bundle
    fun setComponentName(componentName: String): Builder {
      mComponentName = componentName
      return this
    }

    fun setLaunchOptions(launchOptions: Bundle): Builder {
      mLaunchOptions = launchOptions
      return this
    }

    fun build(): HyperswitchFragment {
      return newInstance(
        mComponentName,
        mLaunchOptions
      )
    }
  }

  companion object {
    private fun newInstance(componentName: String, launchOptions: Bundle): HyperswitchFragment {
      val fragment = HyperswitchFragment()
      val args = Bundle()
      args.putString("arg_component_name", componentName)
      args.putBundle("arg_launch_options", launchOptions)
      fragment.arguments = args
      return fragment
    }
  }
}
