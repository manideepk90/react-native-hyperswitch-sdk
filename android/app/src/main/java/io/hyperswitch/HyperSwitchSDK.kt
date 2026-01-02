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
import io.hyperswitch.react.HyperSwitchPackage

class HyperSwitchSDK(
    private val reactHost: ReactHost, private val reactNativeHost: ReactNativeHost
) {

    public fun getReactHost(): ReactHost {
        return reactHost
    }

    public fun getReactNativeHost(): ReactNativeHost {
        return reactNativeHost
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

            val useDeveloperSupport = true
            val reactHost: ReactHost by lazy {
                getDefaultReactHost(
                    context = application,
                    packageList = packageList,
                    jsMainModulePath = "index",
                    jsBundleAssetPath = "hyperswitch.bundle",
                    jsBundleFilePath = "assets://hyperswitch.bundle",
                    useDevSupport = useDeveloperSupport,
                    jsRuntimeFactory = HermesInstance()
                )
            }
            val reactNativeHost = object : DefaultReactNativeHost(application) {
                override fun getPackages(): List<ReactPackage> = packageList
                override fun getJSMainModuleName(): String = "index"
                override fun getJSBundleFile(): String {
                    return "assets://hyperswitch.bundle"
                }

                override fun getUseDeveloperSupport(): Boolean = useDeveloperSupport
                override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
                override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
            }
            init(application, reactHost, reactNativeHost)

        }

        fun init(application: Application, enableOTA: Boolean = true) {
            init(application, packageList = PackageList(application).packages.apply {
                add(HyperSwitchPackage())
            }, enableOTA)
        }


        /*
        * Function that init the SDK with OTA as true
        * */
        fun init(application: Application) {
            init(application, enableOTA = true)
        }
    }


}
