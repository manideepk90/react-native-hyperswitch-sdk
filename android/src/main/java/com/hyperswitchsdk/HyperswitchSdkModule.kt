package com.hyperswitchsdk
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.facebook.react.ReactActivity
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.module.annotations.ReactModule
import com.hyperswitchsdk.log.Logger
import com.hyperswitchsdk.react.Utils


@ReactModule(name = HyperswitchSdkModule.NAME)
class HyperswitchSdkModule(reactContext: ReactApplicationContext) :
  NativeHyperswitchSdkSpec(reactContext) {

  override fun getName(): String {
    return NAME
  }

  fun promiseResultHandler(promise: Promise?, map: ReadableMap) {
    Log.d("MY_CALLBACK_HANDLER", "called")
    try {
      promise?.resolve(map)
    } catch (err: RuntimeException) {
      Log.d("MY_CALLBACK_HANDLER", "flow in catch")
      Log.e("Callback Log--", err.toString())
    }
  }

  override fun initPaymentSession(
    params: ReadableMap,
    promise: Promise
  ) {
    val publishableKey = params.getString("publishableKey")
    Companion.publishableKey = publishableKey ?: ""
    val clientSecret = params.getString("clientSecret")
    val paymentSession = PaymentSession(currentActivity as Activity, publishableKey)
    paymentSession.initPaymentSession(clientSecret ?: "")
    Companion.paymentSession = paymentSession
    val map = Arguments.createMap()
    map.putString("type_", "")
    map.putString("code", "")
    map.putString("message", "initPaymentSession successful")
    map.putString("status", "success")
    promise.resolve(map)
  }


  override fun presentPaymentSheet(sessionParams: ReadableMap, promise: Promise) {
    Log.i("DEBUG_LOG", "reached_here")
    val bundleObj = toBundleObject(sessionParams)
    sheetPromise = promise
    Log.i("Bundle Obj", bundleObj.toString())
    Utils.openReactView(currentActivity as ReactActivity,
      bundleObj, "payment", null)
  }

  override fun getCustomerSavedPaymentMethods(sessionParams: ReadableMap, promise: Promise) {
    promise.resolve("Customer saved payment methods retrieved")
  }

  override fun getCustomerDefaultSavedPaymentMethodData(
    sessionParams: ReadableMap,
    promise: Promise
  ) {
    promise.resolve("Customer default saved payment method data retrieved")
  }

  override fun getCustomerLastUsedPaymentMethodData(
    sessionParams: ReadableMap,
    promise: Promise
  ) {
    promise.resolve("Customer last used payment method data retrieved")
  }

  override fun getCustomerSavedPaymentMethodData(sessionParams: ReadableMap, promise: Promise) {
    promise.resolve("Customer saved payment method data retrieved")
  }

  override fun confirmWithCustomerDefaultPaymentMethod(
    sessionParams: ReadableMap,
    cvc: String?,
    promise: Promise
  ) {
    TODO("Not yet implemented")
  }

  override fun confirmWithCustomerLastUsedPaymentMethod(
    sessionParams: ReadableMap,
    cvc: String?,
    promise: Promise
  ) {
    TODO("Not yet implemented")
  }

  override fun confirmWithCustomerPaymentToken(
    sessionParams: ReadableMap,
    cvc: String?,
    paymentToken: String,
    promise: Promise
  ) {
    TODO("Not yet implemented")
  }


  companion object {
    const val NAME = "NativeHyperswitchSdk"

      @JvmStatic
      lateinit var paymentSession: PaymentSession

      @JvmStatic
      lateinit var paymentSessionHandler: PaymentSessionHandler

      @JvmStatic
      lateinit var googlePayCallback: Callback

      @JvmStatic
      lateinit var sheetPromise: Promise

      @JvmStatic
      lateinit var publishableKey: String


      private fun toBundleObject(readableMap: ReadableMap): Bundle {
        val result = Bundle()
        val iterator = readableMap.keySetIterator()
        while (iterator.hasNextKey()) {
          val key = iterator.nextKey()
          when (readableMap.getType(key)) {
            ReadableType.Null -> result.putString(key, null)
            ReadableType.Boolean -> result.putBoolean(key, readableMap.getBoolean(key))
            ReadableType.Number -> result.putDouble(key, readableMap.getDouble(key))
            ReadableType.String -> result.putString(key, readableMap.getString(key))
            ReadableType.Map -> {
              val nestedMap = readableMap.getMap(key)
              if (nestedMap != null) {
                result.putBundle(key, toBundleObject(nestedMap))
              }
            }
            else -> result.putString(key, readableMap.getString(key))
          }
        }
        return result
      }
    }
}
