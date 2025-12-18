package io.hyperswitch.demoapp

import android.app.Application
import io.hyperswitch.HyperSwitchSDK

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        HyperSwitchSDK.init(this)
    }
}