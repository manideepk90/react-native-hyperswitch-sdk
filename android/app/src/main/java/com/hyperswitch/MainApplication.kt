package com.hyperswitch

import android.app.Application
import com.facebook.react.ReactNativeApplicationEntryPoint.loadReactNative

class MainApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    loadReactNative(this)
  }
}
