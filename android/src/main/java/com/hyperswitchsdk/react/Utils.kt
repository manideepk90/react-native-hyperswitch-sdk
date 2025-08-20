package com.hyperswitchsdk.react
import android.util.Log
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class Utils {
  companion object {
    @JvmStatic
    lateinit var reactNativeFragmentCard: ReactFragment
    @JvmStatic
    var reactNativeFragmentSheet: ReactFragment? = null
    @JvmStatic
    var lastRequest: Bundle? = null
    @JvmStatic
    var flags: Int = 0

    /**
     *
     * @param message message
     * @param clientSecret client secret
     * @param configuration Configuration
     */
    fun openReactView(
      context: AppCompatActivity,
      request: Bundle,
      message: String,
      id: Int?,
      isHidden: Boolean? = false
    ) {
      context.runOnUiThread {
        try {
          val userAgent = getUserAgent(context)
          val ipAddress = getDeviceIPAddress(context)
          context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

          if (message != "card" && message != "google_pay" && message != "paypal") {
            flags = context.window.attributes.flags
            if (message != "unifiedCheckout") {
              context.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
              context.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            } else {
              context.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
              context.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }

            if (reactNativeFragmentSheet == null) {
              lastRequest = request
              reactNativeFragmentSheet = ReactFragment.Builder()
                .setComponentName("ExampleApp")
                .setLaunchOptions(
                  getLaunchOptions(
                    request,
                    message,
                    context.packageName,
                    context.resources.configuration.locale.country,
                    userAgent,
                    ipAddress
                  )
                )
                .build()
              val transaction = context.supportFragmentManager.beginTransaction()
              if (isHidden == true) {
                transaction.hide(reactNativeFragmentSheet!!)
              }
              transaction.add(android.R.id.content, reactNativeFragmentSheet!!)
                .commit()

            } else if (areBundlesNotEqual(request, lastRequest)) {
              // Remove existing fragment first
              val removeTransaction = context.supportFragmentManager.beginTransaction()
              removeTransaction.remove(reactNativeFragmentSheet!!)
              removeTransaction.commitNowAllowingStateLoss()

              lastRequest = request
              reactNativeFragmentSheet = ReactFragment.Builder()
                .setComponentName("ExampleApp")
                .setLaunchOptions(
                  getLaunchOptions(
                    request,
                    message,
                    context.packageName,
                    context.resources.configuration.locale.country,
                    userAgent,
                    ipAddress
                  )
                )

                .build()
              val addTransaction = context.supportFragmentManager.beginTransaction()
              if (isHidden == true) {
                addTransaction.hide(reactNativeFragmentSheet!!)
              }
              addTransaction.add(android.R.id.content, reactNativeFragmentSheet!!).commit()
            } else {
              if (isHidden == true) {
                val transaction = context.supportFragmentManager.beginTransaction()
                transaction.hide(reactNativeFragmentSheet!!)
                transaction.commitAllowingStateLoss()
              }
            }
          } else {
            flags = 0
            reactNativeFragmentCard = ReactFragment.Builder()
              .setComponentName("ExampleApp")
              .setLaunchOptions(
                getLaunchOptions(
                  request,
                  message,
                  context.packageName,
                  context.resources.configuration.locale.country,
                  userAgent,
                  ipAddress
                )
              )
              .build()

            val transaction = context.supportFragmentManager.beginTransaction()
            transaction.add(id ?: android.R.id.content, reactNativeFragmentCard).commit()
          }

          context.supportFragmentManager
            .addFragmentOnAttachListener { _, _ ->
              try {
                context.savedStateRegistry.unregisterSavedStateProvider("android:support:fragments")
              } catch (e: Exception) {
                Log.w("HyperswitchSDK", "Failed to unregister saved state provider", e)
              }
            }
        } catch (e: Exception) {
          Log.e("HyperswitchSDK", "Error opening React view", e)
        }
      }
    }
    fun openReactViewInBackground(
      context: AppCompatActivity,
      request: Bundle,
      message: String,
      id: Int?,
      isHidden: Boolean? = false
    ) {
      context.runOnUiThread {
        try {
          val userAgent = getUserAgent(context)
          val ipAddress = getDeviceIPAddress(context)
          val launchOptions = getLaunchOptions(
            request,
            message,
            context.packageName,
            context.resources.configuration.locale.country,
            userAgent,
            ipAddress
          )

          if (reactNativeFragmentSheet == null) {
            lastRequest = request
            reactNativeFragmentSheet = ReactFragment.Builder()
              .setComponentName("ExampleApp")
              .setLaunchOptions(launchOptions)
              .build()
          } else if (areBundlesNotEqual(request, lastRequest)) {
            lastRequest = request
            reactNativeFragmentSheet = ReactFragment.Builder()
              .setComponentName("ExampleApp")
              .setLaunchOptions(launchOptions)
              .build()
          }

          Log.i("HyperswitchSDK", "Fragment prepared in background")
        } catch (e: Exception) {
          Log.e("HyperswitchSDK", "Error preparing React view in background", e)
        }
      }
    }

    private fun areBundlesNotEqual(bundle1: Bundle?, bundle2: Bundle?): Boolean {
      if (bundle1 == null || bundle2 == null) {
        return true
      }
      if (bundle1.getString("publishableKey") == bundle2.getString("publishableKey")
        && bundle1.getString("clientSecret") == bundle2.getString("clientSecret")
        && bundle1.getString("type") == bundle2.getString("type")
      ) {
        return false
      }
      return true
    }


    fun getUserAgent(context: Context): String {
      return try {
        WebSettings.getDefaultUserAgent(context)
      } catch (e: RuntimeException) {
        System.getProperty("http.agent") ?: ""
      }
    }


    /**
     *
     * @param message message
     * @param request client secret params
     */

    private fun getLaunchOptions(
      request: Bundle,
      message: String,
      packageName: String,
      country: String,
      userAgent: String,
      ipAddress : String
    ): Bundle {
      request.putString("type", message)
      val hyperParams = Bundle()
      Log.i("Package Name---->", packageName)
      hyperParams.putString("appId", packageName)
      hyperParams.putString("country", country)
      hyperParams.putString("user-agent", userAgent)
      hyperParams.putString("ip", ipAddress)
      hyperParams.putDouble("launchTime", getCurrentTime())
      hyperParams.putString("sdkVersion", "")
      hyperParams.putString("device_model", Build.MODEL)
      hyperParams.putString("os_type", "android")
      hyperParams.putString("os_version", Build.VERSION.RELEASE)
      hyperParams.putString("deviceBrand", Build.BRAND)
      request.putBundle("hyperParams", hyperParams)
      val bundle = Bundle()
      bundle.putBundle("props", request)
      return bundle
    }

    fun hideFragment(context: AppCompatActivity, reset: Boolean) {
      if (reactNativeFragmentSheet != null) {
        context.supportFragmentManager
          .beginTransaction()
//          .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom)
          .hide(reactNativeFragmentSheet!!)
          .commit()
      }
      context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
      if (flags != 0) {
        context.runOnUiThread {
          context.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
          context.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
          context.window.addFlags(flags)
        }
      }
      if (reset) {
        reactNativeFragmentSheet = null
      }
    }

    fun onBackPressed(): Boolean {
      return if (reactNativeFragmentSheet == null || reactNativeFragmentSheet!!.isHidden) {
        false
      } else {
        reactNativeFragmentSheet!!.onBackPressed()
        true
      }
    }

    fun getCurrentTime(): Double {
      return System.currentTimeMillis().toDouble()
    }

    // Get device IP address
    fun getDeviceIPAddress(context: Context): String {
      return try {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        String.format(
          Locale.getDefault(), "%d.%d.%d.%d",
          ipAddress and 0xff,
          ipAddress shr 8 and 0xff,
          ipAddress shr 16 and 0xff,
          ipAddress shr 24 and 0xff
        )
      } catch (e: SecurityException) {
        Log.w("HyperswitchSDK", "ACCESS_WIFI_STATE permission not granted, returning default IP")
        "0.0.0.0"
      } catch (e: Exception) {
        Log.w("HyperswitchSDK", "Failed to get IP address: ${e.message}")
        "0.0.0.0"
      }
    }
  }
}
