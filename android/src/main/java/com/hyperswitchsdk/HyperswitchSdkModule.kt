package com.hyperswitchsdk

import android.os.Bundle
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.module.annotations.ReactModule
import io.hyperswitch.PaymentSession
import io.hyperswitch.PaymentSessionHandler


@ReactModule(name = HyperswitchSdkModule.NAME)
class HyperswitchSdkModule(reactContext: ReactApplicationContext) :
  NativeHyperswitchSdkSpec(reactContext) {
  companion object {
    const val NAME = "HyperModule"

    @JvmStatic
    lateinit var paymentSession: PaymentSession

    @JvmStatic
    lateinit var paymentSessionHandler: PaymentSessionHandler

//    @JvmStatic
//    lateinit var googlePayCallback: Callback
//
//    @JvmStatic
//    lateinit var sheetCallback: Callback

    @JvmStatic
    lateinit var publishableKey: String


    private fun toBundleObject(readableMap: ReadableMap?): Bundle {
      val result = Bundle()
      return if (readableMap == null) {
        result
      } else {
        val iterator = readableMap.keySetIterator()
        while (iterator.hasNextKey()) {
          val key = iterator.nextKey()
          when (readableMap.getType(key)) {
            ReadableType.Null -> result.putString(key, null)
            ReadableType.Boolean -> result.putBoolean(key, readableMap.getBoolean(key))
            ReadableType.Number -> result.putDouble(key, readableMap.getDouble(key))
            ReadableType.String -> result.putString(key, readableMap.getString(key))
            ReadableType.Map -> result.putBundle(key, toBundleObject(readableMap.getMap(key)))
            else -> result.putString(key, readableMap.getString(key))
          }
        }
        result
      }
    }
  }
  override fun getName(): String {
    return NAME
  }


  override fun initPaymentSession(params: ReadableMap?, promise: Promise?) {
    val publishableKey = params?.getString("publishableKey")
    Companion.publishableKey = publishableKey ?: ""
    val clientSecret = request.getString("clientSecret")
    val paymentSession = PaymentSession(currentActivity as Activity, publishableKey)
    paymentSession.initPaymentSession(clientSecret ?: "")
    Companion.paymentSession = paymentSession

    val map = Arguments.createMap()
    map.putString("type_", "")
    map.putString("code", "")
    map.putString("message", "initPaymentSession successful")
    map.putString("status", "success")

    callBackResultHandler(callBack, map)
  }

  override fun presentPaymentSheet(sessionParams: ReadableMap?, promise: Promise?) {
    TODO("Not yet implemented")
  }

  override fun getCustomerSavedPaymentMethods(sessionParams: ReadableMap?, promise: Promise?) {
    TODO("Not yet implemented")
  }

  override fun getCustomerDefaultSavedPaymentMethodData(
    sessionParams: ReadableMap?,
    promise: Promise?
  ) {
    TODO("Not yet implemented")
  }

  override fun getCustomerLastUsedPaymentMethodData(
    sessionParams: ReadableMap?,
    promise: Promise?
  ) {
    TODO("Not yet implemented")
  }

  override fun getCustomerSavedPaymentMethodData(sessionParams: ReadableMap?, promise: Promise?) {
    TODO("Not yet implemented")
  }

  override fun confirmWithCustomerDefaultPaymentMethod(params: ReadableMap?, promise: Promise?) {
    TODO("Not yet implemented")
  }

  override fun confirmWithCustomerLastUsedPaymentMethod(params: ReadableMap?, promise: Promise?) {
    TODO("Not yet implemented")
  }

  override fun confirmWithCustomerPaymentToken(params: ReadableMap?, promise: Promise?) {
    TODO("Not yet implemented")
  }


  companion object {
    const val NAME = "HyperswitchSdk"
  }
}
