package io.hyperswitch

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.facebook.react.PackageList
import com.facebook.react.ReactFragment
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeApplicationEntryPoint.loadReactNative
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultReactHost
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.runtime.hermes.HermesInstance

class HyperSwitchSDK(
    private val reactHost: ReactHost, private val reactNativeHost: ReactNativeHost
) {

    public fun getReactHost(): ReactHost {
        return reactHost
    }

    public fun getReactNativeHost(): ReactNativeHost {
        return reactNativeHost
    }

    fun presentFragment(activity: Activity) {
        presentFragment(activity, emptyMap())
    }

    fun presentFragment(activity: Activity, props: Map<String, Any>) {
        val propsBundle = Bundle().apply {
            putString("type", "payment")
            putString("from", "rn")

            props.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Double -> putDouble(key, value)
                }
            }
        }
        val hyperSwitchFragment = HyperswitchFragment.Builder().setComponentName("hyperSwitch")
            .setLaunchOptions(propsBundle).build()

        val fragmentManager: FragmentManager = (activity as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(android.R.id.content, hyperSwitchFragment, "HyperPaymentSheet")
        fragmentTransaction.addToBackStack("HyperPaymentSheet")
        fragmentTransaction.commit()
    }

    companion object {
        lateinit var shared: HyperSwitchSDK

        private var isInitialized = false

        private var isSoLoaderInit = false

        private fun initReactNative(application: Application) {
            if (!isSoLoaderInit) {
                loadReactNative(application)
                isSoLoaderInit = true
            }
        }

        fun init(application: Application, reactHost: ReactHost, reactNativeHost: ReactNativeHost) {
            if (!isInitialized) {
                initReactNative(application)
                shared = HyperSwitchSDK(reactHost, reactNativeHost)
                isInitialized = true
            }
        }

        /*
        Remember to call the InitReactNative before creating the ReactHost and reactNativeHost
         */
        fun init(application: Application, packageList: List<ReactPackage>, enableOTA: Boolean) {
            initReactNative(application)

            val useDeveloperSupport = BuildConfig.DEBUG
            val reactHost: ReactHost by lazy {
                getDefaultReactHost(
                    context = application,
                    packageList =
                        PackageList(application).packages,
                    jsMainModulePath = "index",
                    jsBundleAssetPath = "hyperswitch.bundle",
                    jsBundleFilePath = "assets://hyperswitch.bundle",
                    useDevSupport = BuildConfig.DEBUG,
                    jsRuntimeFactory = HermesInstance()
                )
            }
            val reactNativeHost = object : DefaultReactNativeHost(application) {
                override fun getPackages(): List<ReactPackage> = packageList
                override fun getJSMainModuleName(): String = "index"
                override fun getJSBundleFile(): String? {
                    return "assets://hyperswitch.bundle"
                }

                override fun getUseDeveloperSupport(): Boolean = useDeveloperSupport
                override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
                override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
            }
            init(application, reactHost, reactNativeHost)

        }

        fun init(application: Application, enableOTA: Boolean = true) {
            init(application, packageList = PackageList(application).packages, enableOTA)
        }

        fun init(application: Application) {
            init(application, enableOTA = true)
        }
    }


}
