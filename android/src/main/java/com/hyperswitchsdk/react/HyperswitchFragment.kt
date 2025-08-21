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
import com.facebook.react.modules.core.PermissionAwareActivity
import com.facebook.react.modules.core.PermissionListener
import com.facebook.react.shell.MainReactPackage
import com.hyperswitchsdk.HyperswitchSdkPackage


open class HyperswitchFragment : ReactFragment(),
  PermissionAwareActivity {
  private var reactDelegate: com.facebook.react.ReactDelegate? = null
  private var mPermissionListener: PermissionListener? = null
  private var originalSoftInputMode: Int = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
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

    val mReactRootView = ReactRootView(context)
//    val packages = listOf<ReactPackage>(MainReactPackage())

     val packages = listOf<ReactPackage>(
       MainReactPackage(),
       HyperswitchSdkPackage()
     )
//    HyperPackageList(activity?.application, context).packages
//    packages.add(ReactNativeHyperswitchPackage())

    val mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(activity?.application)
      .setCurrentActivity(activity)
      .addPackages(packages)
      .setBundleAssetName("hyperswitch.bundle") // Make sure this matches your bundle name
      .setJSMainModulePath("index") // Make sure this matches your entry file
//      .setJSBundleFile("assets://hyperswitch.bundle") // Can be redundant if setBundleAssetName is used correctly
      .setJavaScriptExecutorFactory(HermesExecutorFactory())
      .setUseDeveloperSupport(false)
      .setInitialLifecycleState(LifecycleState.RESUMED) // Changed to RESUMED
      .setReactPackageTurboModuleManagerDelegateBuilder(DefaultTurboModuleManagerDelegate.Builder())
      .build()

    var mainComponentName: String? = null
    var launchOptions: Bundle? = null
    if (this.arguments != null) {
      mainComponentName = this.requireArguments().getString("arg_component_name")
      launchOptions = this.requireArguments().getBundle("arg_launch_options")
    }
    checkNotNull(mainComponentName) { "Cannot loadApp if component name is null" }

    mReactRootView.startReactApplication(mReactInstanceManager, mainComponentName, launchOptions)

//    val catalystInstance: CatalystInstance? = (activity?.application as ReactApplication ).reactHost?.currentReactContext?.catalystInstance
//    val jsAppModuleName = "hyperSwitch"
//
//    val appParams = WritableNativeMap()
//    appParams.putInt("rootTag", 1)
//    @Nullable val appProperties = Bundle.EMPTY
//    if (appProperties != null) {
//      appParams.putMap("initialProps", Arguments.fromBundle(appProperties))
//    }
//    val bundleLoader = JSBundleLoader.createAssetLoader(requireContext(), "hyperswitch.bundle", false)
//
//    catalystInstance?.loadScriptFromAssets(assetManager = requireContext().assets , "assets://hyperswitch.bundle", loadSynchronously = false)
//
//    catalystInstance?.getJSModule<AppRegistry>(AppRegistry::class.java)!!
//      .runApplication(mainComponentName, appParams)

//    originalSoftInputMode = activity?.window?.attributes?.softInputMode ?: WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    return mReactRootView
//    reactDelegate?.loadApp()

//    return reactDelegate?.reactRootView
  }

  private fun setSoftInputMode(inputMode: Int) {
    if(originalSoftInputMode != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
      activity?.window?.setSoftInputMode(inputMode)
  }

  override fun onResume() {
    super.onResume()
    reactDelegate?.onHostResume() // Use reactDelegate here if you intend to use it
    // If you are fully managing with ReactInstanceManager, ensure its lifecycle methods are called
    // mReactInstanceManager.onHostResume(activity, this)
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
  }

  override fun onPause() {
    super.onPause()
    reactDelegate?.onHostPause() // Use reactDelegate here
    // If you are fully managing with ReactInstanceManager, ensure its lifecycle methods are called
    // mReactInstanceManager.onHostPause()
    setSoftInputMode(originalSoftInputMode)
  }

  override fun onDestroy() {
    super.onDestroy()
    reactDelegate?.onHostDestroy() // Use reactDelegate here
    // If you are fully managing with ReactInstanceManager, ensure its lifecycle methods are called
    // mReactInstanceManager.onHostDestroy()
    // mReactRootView.unmountReactApplication() // Important for cleanup
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
    reactDelegate?.onActivityResult(requestCode, resultCode, data, false)
    // If you are fully managing with ReactInstanceManager, ensure its lifecycle methods are called
    // mReactInstanceManager.onActivityResult(activity, requestCode, resultCode, data)
  }

  override fun onBackPressed(): Boolean {
    // If using ReactInstanceManager directly, you might need to handle this differently
    // reactDelegate handles this internally.
    return reactDelegate?.onBackPressed() ?: false
  }

  override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        // This is typically for dev menu, which you have disabled via reactDelegate.
        // If mReactInstanceManager is primary, this might need:
        // mReactInstanceManager.onKeyUp(keyCode, event)
        // However, with dev support false, it might not be needed or could be handled by reactDelegate.
    return reactDelegate?.shouldShowDevMenuOrReload(keyCode, event) ?: false
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
