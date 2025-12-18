package io.hyperswitch.demoapp

import android.app.Application
import com.rnhyperswitch.HyperSwitchSDK

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        HyperSwitchSDK.init(this)
    }
}