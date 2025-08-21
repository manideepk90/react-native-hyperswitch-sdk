//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.hyperswitchsdk.react

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.common.annotations.UnstableReactNativeAPI
import com.facebook.react.defaults.DefaultReactHost
import com.facebook.react.defaults.DefaultReactHostDelegate
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.defaults.DefaultTurboModuleManagerDelegate
import com.facebook.react.fabric.ComponentFactory
import com.facebook.react.internal.featureflags.ReactNativeFeatureFlags.enableBridgelessArchitecture
import com.facebook.react.modules.core.PermissionAwareActivity
import com.facebook.react.modules.core.PermissionListener
import com.facebook.react.runtime.ReactHostImpl
import com.facebook.react.runtime.hermes.HermesInstance
import com.facebook.react.shell.MainReactPackage
import com.hyperswitchsdk.HyperswitchSdkPackage


class ReactFragment : Fragment(), PermissionAwareActivity {
  protected var mReactDelegate: ReactDelegate? = null
  private var mDisableHostLifecycleEvents = false
  private var mPermissionListener: PermissionListener? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var mainComponentName: String? = null
    var launchOptions: Bundle? = null
    var fabricEnabled: Boolean? = null

    mainComponentName = this.requireArguments().getString("arg_component_name")
    launchOptions = this.requireArguments().getBundle("arg_launch_options")
    fabricEnabled = false
    this.mDisableHostLifecycleEvents =
      this.requireArguments().getBoolean("arg_disable_host_lifecycle_events")


    checkNotNull(mainComponentName != null) { "Cannot loadApp if component name is null" }
    if (enableBridgelessArchitecture()) {
      Log.i("ReactFragment", "Using New Architecture (Bridgeless) for component: $mainComponentName")
      Log.i("ReactFragment", "Launch options: ${launchOptions?.keySet()?.joinToString(", ") ?: "null"}")
      this.mReactDelegate = ReactDelegate(
        this.requireActivity(),
        this.reactHost, mainComponentName!!, launchOptions
      )
    } else {
      this.mReactDelegate = ReactDelegate(
        this.requireActivity(),
        this.reactNativeHost, mainComponentName!!, launchOptions, fabricEnabled!!
      )
    }
  }

  protected val reactNativeHost: ReactNativeHost?
    get() {
      val reactNativeHost: ReactNativeHost =
        object : DefaultReactNativeHost(requireActivity().application) {
          override fun getPackages(): List<ReactPackage> = listOf( MainReactPackage(null))
//            PackageList(this).packages.apply {
//              // Packages that cannot be autolinked yet can be added manually here, for example:
//              add(HyperswitchSdkPackage())
//            }


          override fun getJSBundleFile(): String? {
            return "assets://hyperswitch.bundle"
          }

          override fun getBundleAssetName(): String? {
            return "hyperswitch.bundle"
          }

          override fun getJSMainModuleName(): String = "index"

          override fun getUseDeveloperSupport(): Boolean {
            return false
          }

          override val isNewArchEnabled: Boolean = true//BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
          override val isHermesEnabled: Boolean = true//BuildConfig.IS_HERMES_ENABLED
        }

      return reactNativeHost
    }

  @OptIn(UnstableReactNativeAPI::class)
  protected val reactHost: ReactHost?
    get() {
      val application = requireActivity().application

      val packages: List<ReactPackage> = listOf(
        MainReactPackage(),
//        HyperswitchSdkPackage()
      )
      val bundleLoader = JSBundleLoader.createAssetLoader(requireContext(), "hyperswitch.bundle", false)
      val defaultReactHostDelegate = DefaultReactHostDelegate(
        jsMainModulePath = "index2",
        jsBundleLoader = bundleLoader,
        reactPackages = packages,
        jsRuntimeFactory = HermesInstance(),
        bindingsInstaller = null,
        exceptionHandler = { exception ->
          Log.e("ReactHost", "React Native exception: ${exception.message}", exception)
          Log.e("ReactHost", "Stack trace: ${exception.stackTrace.joinToString("\n")}")
          Log.e("ReactHost", "Bundle loading failed - check if hyperswitch.bundle exists and contains hyperSwitch component")
          Log.e("ReactHost", "Exception type: ${exception.javaClass.simpleName}")
        },
        turboModuleManagerDelegateBuilder = DefaultTurboModuleManagerDelegate.Builder()
      )

      // Try-catch around ReactHost creation to handle ComponentFactory issues
      val reactHost = try {
        Log.i("ReactHost", "Creating ReactHostImpl with New Architecture")
        ReactHostImpl(
          requireContext(),
          defaultReactHostDelegate,
          ComponentFactory(),
          false, // Disable dev support for production bundle
          false // Disable lazy view managers initialization
        )
      } catch (e: Exception) {
        Log.e("ReactHost", "Failed to create ReactHostImpl: ${e.message}", e)
        // Fallback: try with different parameters
        Log.i("ReactHost", "Attempting fallback ReactHostImpl configuration")
        ReactHostImpl(
          requireContext(),
          defaultReactHostDelegate,
          ComponentFactory(),
          false,
          true // Try with lazy view managers enabled
        )
      }

      // DefaultReactHost is instantiated directly with its configuration.
//      val reactHost = DefaultReactHost.getDefaultReactHost(
//        context = application,
//        packageList = packages,
//        jsMainModulePath = "index", // Your JS module name
//        jsBundleAssetPath = "assets://hyperswitch.bundle", // Bundle asset path
//        jsBundleFilePath = "assets://hyperswitch.bundle", // Optional file path
//        jsRuntimeFactory = HermesInstance(), // Hermes enabled
//        useDevSupport = false,
//        cxxReactPackageProviders = emptyList()
//      )
      return reactHost
    }

  protected val reactDelegate: ReactDelegate
    get() = this.mReactDelegate!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    this.mReactDelegate!!.loadApp()
    return this.mReactDelegate!!.reactRootView
  }

  override fun onResume() {
    super.onResume()
    if (!this.mDisableHostLifecycleEvents) {
      this.mReactDelegate!!.onHostResume()
    }
  }

  override fun onPause() {
    super.onPause()
    if (!this.mDisableHostLifecycleEvents) {
      this.mReactDelegate!!.onHostPause()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    if (!this.mDisableHostLifecycleEvents) {
      this.mReactDelegate!!.onHostDestroy()
    } else {
      this.mReactDelegate!!.unloadApp()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    this.mReactDelegate!!.onActivityResult(requestCode, resultCode, data, false)
  }

  fun onBackPressed(): Boolean {
    return this.mReactDelegate!!.onBackPressed()
  }

  fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
    return this.mReactDelegate!!.shouldShowDevMenuOrReload(keyCode, event)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (this.mPermissionListener != null && this.mPermissionListener!!.onRequestPermissionsResult(
        requestCode,
        permissions,
        grantResults
      )
    ) {
      this.mPermissionListener = null
    }
  }

  override fun checkPermission(permission: String, pid: Int, uid: Int): Int {
    return this.requireActivity().checkPermission(permission, pid, uid)
  }

  override fun checkSelfPermission(permission: String): Int {
    return this.requireActivity().checkSelfPermission(permission)
  }

  override fun requestPermissions(
    permissions: Array<String>,
    requestCode: Int,
    listener: PermissionListener?
  ) {
    this.mPermissionListener = listener
    this.requestPermissions(permissions, requestCode)
  }

  class Builder {
    var mComponentName: String? = null
    var mLaunchOptions: Bundle? = null
    var mFabricEnabled: Boolean? = false

    fun setComponentName(componentName: String?): Builder {
      this.mComponentName = componentName
      return this
    }

    fun setLaunchOptions(launchOptions: Bundle?): Builder {
      this.mLaunchOptions = launchOptions
      return this
    }

    fun build(): ReactFragment {
      return Companion.newInstance(this.mComponentName, this.mLaunchOptions, this.mFabricEnabled!!)
    }

    fun setFabricEnabled(fabricEnabled: Boolean): Builder {
      this.mFabricEnabled = fabricEnabled
      return this
    }
  }

  companion object {
    protected const val ARG_COMPONENT_NAME: String = "arg_component_name"
    protected const val ARG_LAUNCH_OPTIONS: String = "arg_launch_options"
    protected const val ARG_FABRIC_ENABLED: String = "arg_fabric_enabled"

    @Deprecated("")
    protected const val ARG_DISABLE_HOST_LIFECYCLE_EVENTS: String =
      "arg_disable_host_lifecycle_events"

    private fun newInstance(
      componentName: String?,
      launchOptions: Bundle?,
      fabricEnabled: Boolean
    ): ReactFragment {
      val fragment = ReactFragment()
      val args = Bundle()
      args.putString("arg_component_name", componentName)
      args.putBundle("arg_launch_options", launchOptions)
      args.putBoolean("arg_fabric_enabled", fabricEnabled)
      fragment.setArguments(args)
      return fragment
    }
  }
}
