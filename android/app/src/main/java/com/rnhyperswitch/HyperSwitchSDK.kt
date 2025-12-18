package com.rnhyperswitch

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
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.runtime.hermes.HermesInstance

class HyperSwitchSDK(
    private val reactNativeHost: ReactNativeHost, private val reactHost: ReactHost
) {

    public fun getReactHost(): ReactHost {
        return reactHost
    }

    public fun getReactNativeHost(): ReactNativeHost {
        return reactNativeHost
    }

    fun presentFragment(activity: Activity) {
        val propsBundle = Bundle().apply {
            putString("type", "payment")
            putString("from", "rn")
        }
        val hyperSwitchFragment = HyperswitchFragment.Builder().setComponentName("RNHyperSwitch").setLaunchOptions(propsBundle).build()

        val fragmentManager: FragmentManager = (activity as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(android.R.id.content,hyperSwitchFragment, "HyperPaymentSheet")
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
                shared = HyperSwitchSDK(reactNativeHost, reactHost)
                isInitialized = true
            }
        }

        /*
        Remember to call the InitReactNative before creating the ReactHost and reactNativeHost
         */
        fun init(application: Application, packageList: List<ReactPackage>, enableOTA: Boolean) {
            initReactNative(application)

            val reactHost = DefaultReactHost.getDefaultReactHost(
                application,
                packageList,
                "index",
                "hyperswitch",
                "assets://hyperswitch.bundle", // TODO need to OTA here
                HermesInstance(),
                BuildConfig.DEBUG
            )
            val reactNativeHost = object : DefaultReactNativeHost(application) {
                override fun getPackages(): List<ReactPackage> = packageList

                override fun getJSMainModuleName(): String = "index"
                override fun getJSBundleFile(): String {
                    return "assets://hyperswitch.bundle" // TODO need to OTA here
                }

                override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

                override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
                override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED

//                @Deprecated(
//                    "Setting isHermesEnabled inside `ReactNativeHost` is deprecated and this field will be ignored. If this field is set to true, you can safely remove it. If this field is set to false, please follow the setup on https://github.com/react-native-community/javascriptcore to continue using JSC",
//                    replaceWith = ReplaceWith("")
//                )
//                override val isHermesEnabled: Boolean = true
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