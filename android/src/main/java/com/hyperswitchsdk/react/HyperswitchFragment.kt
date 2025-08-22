package com.hyperswitchsdk.react

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentActivity
import com.facebook.hermes.reactexecutor.HermesExecutorFactory
import com.facebook.react.*
import com.facebook.react.ReactFragment
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.CatalystInstance
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.common.LifecycleState
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.defaults.DefaultTurboModuleManagerDelegate
import com.facebook.react.modules.appregistry.AppRegistry
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.modules.core.PermissionAwareActivity
import com.facebook.react.modules.core.PermissionListener
import com.facebook.react.shell.MainReactPackage
import com.hyperswitchsdk.HyperswitchSdkPackage


open class HyperswitchFragment : ReactFragment(),
  PermissionAwareActivity {
  private var reactDelegate: com.facebook.react.ReactDelegate? = null
  private var mPermissionListener: PermissionListener? = null
  private var originalSoftInputMode: Int = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
  private var mReactInstanceManager: ReactInstanceManager? = null
  private var mReactRootView: ReactRootView? = null

  companion object {
    private fun createFreshReactInstanceManager(context: android.content.Context, packages: List<ReactPackage>): ReactInstanceManager {
      return ReactInstanceManager.builder()
        .setApplication(context.applicationContext as android.app.Application)
        .addPackages(packages)
        .setBundleAssetName("hyperswitch.bundle")
        .setJSMainModulePath("index")
        .setJavaScriptExecutorFactory(HermesExecutorFactory())
        .setUseDeveloperSupport(false)
        .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
        .setReactPackageTurboModuleManagerDelegateBuilder(DefaultTurboModuleManagerDelegate.Builder())
        .build()
    }

    private fun newInstance(componentName: String, launchOptions: Bundle): HyperswitchFragment {
      val fragment = HyperswitchFragment()
      val args = Bundle()
      args.putString("arg_component_name", componentName)
      args.putBundle("arg_launch_options", launchOptions)
      fragment.arguments = args
      return fragment
    }
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var mainComponentName: String? = null
    var launchOptions: Bundle? = null
    if (this.arguments != null) {
      mainComponentName = this.requireArguments().getString("arg_component_name")
      launchOptions = this.requireArguments().getBundle("arg_launch_options")
    }
    checkNotNull(mainComponentName) { "Cannot loadApp if component name is null" }
//    class DisableBridgelessProvider : ReactNativeFeatureFlagsDefaults() {
//      override fun enableBridgelessArchitecture(): Boolean = false
//    }
//    ReactNativeFeatureFlags.override(DisableBridgelessProvider())
//    val reactNativeHost: ReactNativeHost =
//      object : DefaultReactNativeHost(requireActivity().application) {
//        override fun getPackages(): List<ReactPackage> = listOf<ReactPackage>(MainReactPackage())
//
//        override fun getJSBundleFile(): String? {
//          return "assets://hyperswitch.bundle"
//        }
//
//        override fun getJSMainModuleName(): String = "index2"
//
//        override fun getUseDeveloperSupport(): Boolean {
//          return false // Disable Metro server
//        }
//
//        override val isNewArchEnabled: Boolean = false //BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
//        override val isHermesEnabled: Boolean = true//BuildConfig.IS_HERMES_ENABLED
//      }

//    reactDelegate = ReactDelegate(
//      this.activity,
//      reactNativeHost, mainComponentName, launchOptions
//    )
//    val bundleLoader = JSBundleLoader.createAssetLoader(requireContext(), "assets://hyperswitch.bundle", true)
//    val defaultReactHostDelegate = DefaultReactHostDelegate(
//      jsMainModulePath = "index",
//      jsBundleLoader = bundleLoader,
//      reactPackages = listOf(MainReactPackage()),
//      jsRuntimeFactory = HermesInstance(),
//      bindingsInstaller = null,
//      exceptionHandler =  { exception ->
//        Log.e("ReactHost", "React Native exception", exception)
//        // Handle or rethrow as needed
//      },
//      turboModuleManagerDelegateBuilder = DefaultTurboModuleManagerDelegate.Builder()
//    )
//    val reactHost = ReactHostImpl(
//      requireContext(),
//      defaultReactHostDelegate,
//      ComponentFactory(),
//      false, // Explicitly disable Metro
//      false
//    )
//    val componentFactory = ComponentFactory()
//    val reactHost = DefaultReactHost.getDefaultReactHost(
//      context = requireContext(),
//      packageList = listOf(MainReactPackage()),
//      jsMainModulePath = "index",
//      jsBundleAssetPath = "hyperswitch.bundle",
//      jsBundleFilePath = null,
//      jsRuntimeFactory = HermesInstance(),
//      useDevSupport = false,
//      cxxReactPackageProviders = emptyList(),
//      exceptionHandler = { exception ->
//        Log.e("ReactHost", "React Native exception", exception)
//      },
//      bindingsInstaller = null
//    )
//
//    reactDelegate = ReactDelegate(
//      this.activity,
//      reactHost,
//      mainComponentName,
//      launchOptions
//    )
  }
  @SuppressLint("VisibleForTests")
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    // Clean up any existing ReactRootView
    mReactRootView?.unmountReactApplication()
    mReactRootView = null

    // Create new ReactRootView
    mReactRootView = ReactRootView(requireContext())

    // Define packages
    val packages = listOf<ReactPackage>(

      HyperswitchSdkPackage()
    )

    // Create fresh ReactInstanceManager for each fragment to avoid root tag conflicts
    mReactInstanceManager = createFreshReactInstanceManager(requireContext(), packages)

    // Get component name and launch options
    var mainComponentName: String? = null
    var launchOptions: Bundle? = null
    if (this.arguments != null) {
      mainComponentName = this.requireArguments().getString("arg_component_name")
      launchOptions = this.requireArguments().getBundle("arg_launch_options")
    }
    checkNotNull(mainComponentName) { "Cannot loadApp if component name is null" }

    // Start React application with fresh instance manager
    mReactRootView!!.startReactApplication(mReactInstanceManager, mainComponentName, launchOptions)

    return mReactRootView
  }

  private fun setSoftInputMode(inputMode: Int) {
    if(originalSoftInputMode != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
      activity?.window?.setSoftInputMode(inputMode)
  }

  override fun onResume() {
    super.onResume()
    // Use ReactInstanceManager lifecycle methods instead of reactDelegate
    mReactInstanceManager?.onHostResume(activity)
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
  }

  override fun onPause() {
    super.onPause()
    // Use ReactInstanceManager lifecycle methods instead of reactDelegate
    mReactInstanceManager?.onHostPause(activity)
    setSoftInputMode(originalSoftInputMode)
  }

  override fun onDestroy() {
    super.onDestroy()
    // Properly cleanup ReactRootView and ReactInstanceManager
    try {
      mReactRootView?.unmountReactApplication()
      mReactInstanceManager?.onHostDestroy(activity)
      mReactInstanceManager?.destroy()
    } catch (e: Exception) {
      // Ignore cleanup errors to prevent crashes
    }
    mReactRootView = null
    mReactInstanceManager = null
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
    // Use ReactInstanceManager for activity results
    mReactInstanceManager?.onActivityResult(activity, requestCode, resultCode, data)
  }

  override fun onBackPressed(): Boolean {
    // Handle back press with ReactInstanceManager
    return (mReactInstanceManager?.onBackPressed() ?: false) as Boolean
  }

  override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
    // Handle key events with ReactInstanceManager (for dev menu, etc.)
    return if (mReactInstanceManager != null && activity != null) {
//      mReactInstanceManager!!.onKeyUp(requireActivity(), keyCode)
      true
    } else {
      false
    }
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

//  @TargetApi(23)
//  override fun requestPermissions(
//    permissions: Array<String>,
//    requestCode: Int,
//    listener: PermissionListener
//  ) {
//    mPermissionListener = listener
//    this.requestPermissions(permissions, requestCode)
//  }

  class Builder: ReactFragment.Builder() {
    private lateinit var mComponentName: String
    private lateinit var mLaunchOptions: Bundle
    override fun setComponentName(componentName: String): Builder {
      mComponentName = componentName
      return this
    }

    override fun setLaunchOptions(launchOptions: Bundle): Builder {
      mLaunchOptions = launchOptions
      return this
    }

    override fun build(): HyperswitchFragment {
      return newInstance(
        mComponentName,
        mLaunchOptions
      )
    }
  }

}

