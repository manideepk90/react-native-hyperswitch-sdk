package io.hyperswitch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.react.ReactDelegate
import com.facebook.react.ReactFragment
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.internal.featureflags.ReactNativeFeatureFlags
import com.facebook.react.modules.core.PermissionListener

internal open class HyperswitchFragment: ReactFragment() {
    private var disableHostLifecycleEvents = false
    private var permissionListener: PermissionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mainComponentName: String? = null
        var launchOptions: Bundle? = null
        var fabricEnabled = false
        arguments?.let { args ->
            mainComponentName = args.getString(ARG_COMPONENT_NAME)
            launchOptions = args.getBundle(ARG_LAUNCH_OPTIONS)
            fabricEnabled = args.getBoolean(ARG_FABRIC_ENABLED)
            @Suppress("DEPRECATION")
            disableHostLifecycleEvents = args.getBoolean(ARG_DISABLE_HOST_LIFECYCLE_EVENTS)
        }
        checkNotNull(mainComponentName) { "Cannot loadApp if component name is null" }
        reactDelegate =
            if (ReactNativeFeatureFlags.enableBridgelessArchitecture()) {
                ReactDelegate(requireActivity(), reactHost, mainComponentName, launchOptions)
            } else {
                @Suppress("DEPRECATION")
                (ReactDelegate(
                    requireActivity(),
                    reactNativeHost,
                    mainComponentName,
                    launchOptions,
                    fabricEnabled,
                ))
            }
    }

    override val reactHost: ReactHost?
        get() = HyperSwitchSDK.shared.getReactHost()

    override val reactNativeHost: ReactNativeHost?
        get() = HyperSwitchSDK.shared.getReactNativeHost()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        reactDelegate.loadApp()
        return reactDelegate.reactRootView
    }

    override fun onResume() {
        super.onResume()
        if (!disableHostLifecycleEvents) {
            reactDelegate.onHostResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!disableHostLifecycleEvents) {
            reactDelegate.onHostPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disableHostLifecycleEvents) {
            reactDelegate.onHostDestroy()
        } else {
            reactDelegate.unloadApp()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        @Suppress("DEPRECATION")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionListener?.let {
            if (it.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                permissionListener = null
            }
        }
    }

    override fun checkPermission(permission: String, pid: Int, uid: Int): Int =
        activity?.checkPermission(permission, pid, uid) ?: 0

    override fun checkSelfPermission(permission: String): Int =
        activity?.checkSelfPermission(permission) ?: 0

    @Suppress("DEPRECATION")
    override fun requestPermissions(
        permissions: Array<String>,
        requestCode: Int,
        listener: PermissionListener?,
    ) {
        permissionListener = listener
        requestPermissions(permissions, requestCode)
    }

    /** Builder class to help instantiate a ReactFragment. */
    class Builder {
        var componentName: String? = null
        var launchOptions: Bundle? = null
        var fabricEnabled: Boolean = false

        /**
         * Set the Component name for our React Native instance.
         *
         * @param componentName The name of the component
         * @return Builder
         */
        fun setComponentName(componentName: String): Builder {
            this.componentName = componentName
            return this
        }

        /**
         * Set the Launch Options for our React Native instance.
         *
         * @param launchOptions launchOptions
         * @return Builder
         */
        fun setLaunchOptions(launchOptions: Bundle): Builder {
            this.launchOptions = launchOptions
            return this
        }

        fun build(): HyperswitchFragment = newInstance(componentName, launchOptions, fabricEnabled)

        @Deprecated(
            "You should not change call ReactFragment.setFabricEnabled. Instead enable the NewArchitecture for the whole application with newArchEnabled=true in your gradle.properties file"
        )
        fun setFabricEnabled(fabricEnabled: Boolean): Builder {
            this.fabricEnabled = fabricEnabled
            return this
        }
    }

    companion object {
        protected const val ARG_COMPONENT_NAME: String = "arg_component_name"
        protected const val ARG_LAUNCH_OPTIONS: String = "arg_launch_options"
        protected const val ARG_FABRIC_ENABLED: String = "arg_fabric_enabled"

        @Deprecated(
            "We will remove this and use a different solution for handling Fragment lifecycle events."
        )
        protected const val ARG_DISABLE_HOST_LIFECYCLE_EVENTS: String =
            "arg_disable_host_lifecycle_events"




        /**
         * @param componentName The name of the react native component
         * @param launchOptions The launch options for the react native component
         * @param fabricEnabled Flag to enable Fabric for ReactFragment
         * @return A new instance of fragment ReactFragment.
         */
        private fun newInstance(
            componentName: String?,
            launchOptions: Bundle?,
            fabricEnabled: Boolean,
        ): HyperswitchFragment {
            val args =
                Bundle().apply {
                    putString(ARG_COMPONENT_NAME, componentName)
                    putBundle(ARG_LAUNCH_OPTIONS, launchOptions)
                    putBoolean(ARG_FABRIC_ENABLED, fabricEnabled)
                }
            return HyperswitchFragment().apply { setArguments(args) }
        }
    }

}