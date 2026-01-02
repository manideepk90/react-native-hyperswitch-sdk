package io.hyperswitch.react

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import io.hyperswitch.HyperSwitchSDK
import io.hyperswitch.paymentsession.SDKInterface
import io.hyperswitch.paymentsheet.PaymentSheet
import com.facebook.react.uimanager.PixelUtil
import io.hyperswitch.react.HyperswitchFragment
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import io.hyperswitch.paymentsession.LaunchOptions
import com.facebook.react.common.assets.ReactFontManager

class HyperswitchReactNative(private val activity: Activity) : SDKInterface {
    private val hyperswitchSDK = HyperSwitchSDK
    private val launchOptions = LaunchOptions(activity, "0.1.1")

    override fun presentSheet(
        paymentIntentClientSecret: String,
        configuration: PaymentSheet.Configuration?
    ): Boolean {
        Log.i("Manideep", "Came here bro 1")
        val bundle = launchOptions.getBundle(paymentIntentClientSecret, configuration)
        Log.i("Manideep", "manide ${bundle.toString()}")
        return presentSheet(bundle)
    }

    override fun presentSheet(configurationMap: Map<String, Any?>): Boolean {
        Log.i("Manideep", "Came here bro 2")
        return presentSheet(
            bottomInsetToDIPFromPixel(
                launchOptions.getBundleWithHyperParams(
                    configurationMap
                )
            )
        )
    }

    private fun applyFonts(configuration: PaymentSheet.Configuration?, bundle: Bundle) {
        configuration?.appearance?.typography?.fontResId?.let {
            ReactFontManager.getInstance().addCustomFont(
                activity,
                activity.resources.getResourceName(it).toString().split("/")[1],
                it
            )
            bundle.getBundle("props")?.getBundle("configuration")?.getBundle("appearance")
                ?.getBundle("typography")?.let { typography ->
                    typography.remove("fontResId")
                    typography.putString(
                        "fontResId",
                        activity.resources.getResourceName(it).toString().split("/")[1]
                    )
                }
        }

        configuration?.appearance?.primaryButton?.typography?.fontResId?.let {
            ReactFontManager.getInstance().addCustomFont(
                activity,
                activity.resources.getResourceName(it).toString().split("/")[1],
                it
            )
            bundle.getBundle("configuration")?.getBundle("appearance")?.getBundle("primaryButton")
                ?.getBundle("typography")?.let { typography ->
                    typography.remove("fontResId")
                    typography.putString(
                        "fontResId",
                        activity.resources.getResourceName(it).toString().split("/")[1]
                    )
                }
        }
    }

    private fun presentSheet(bundle: Bundle): Boolean {
        Log.i("Manideep", "finally ${bundle.toString()}")

        if (activity is DefaultHardwareBackBtnHandler && activity is FragmentActivity) {
            val newReactNativeFragmentSheet =
                HyperswitchFragment.Builder().setComponentName("hyperSwitch")
                    .setLaunchOptions(bundle)
                    .build()

            val activity2 = activity as FragmentActivity

//            activity2.onBackPressedDispatcher.addCallback {
//                newReactNativeFragmentSheet.onBackPressed()
//                // activity2.onBackPressedDispatcher.onBackPressed()
//            }

            activity2.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, newReactNativeFragmentSheet, "paymentSheet")
                .commitAllowingStateLoss()

            return true
        } else {
            activity.startActivity(
                Intent(
                    activity.applicationContext, HyperActivity::class.java
                ).apply {
                    putExtra("flow", 1)
                    putExtra("configuration", bundle)
                })

            return false
        }
    }

    private fun bottomInsetToDIPFromPixel(bundle: Bundle): Bundle {

        val propsBundle = bundle.getBundle("props")
        val hyperParamsBundle = propsBundle?.getBundle("hyperParams")
        hyperParamsBundle?.getFloat("topInset")?.let { dipValue ->
            hyperParamsBundle.putFloat("topInset", PixelUtil.toDIPFromPixel(dipValue))
        }

        hyperParamsBundle?.getFloat("leftInset")?.let { dipValue ->
            hyperParamsBundle.putFloat("leftInset", PixelUtil.toDIPFromPixel(dipValue))
        }
        hyperParamsBundle?.getFloat("rightInset")?.let { dipValue ->
            hyperParamsBundle.putFloat("rightInset", PixelUtil.toDIPFromPixel(dipValue))
        }
        hyperParamsBundle?.getFloat("bottomInset")?.let { dipValue ->
            hyperParamsBundle.putFloat("bottomInset", PixelUtil.toDIPFromPixel(dipValue))
        }
        return bundle
    }
    override fun initializeReactNativeInstance() {

    }

    override fun recreateReactContext() {

    }
}