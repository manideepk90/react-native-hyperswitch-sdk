package io.hyperswitch.demoapp

import android.app.Activity
import android.content.Intent
import android.util.Patterns
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.github.kittinunf.fuel.Fuel.reset
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import org.json.JSONObject
import androidx.core.content.edit
import io.hyperswitch.HyperSwitchSDK


class MainActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {
    lateinit var ctx: Activity
    private var publishableKey: String = ""
    private var paymentIntentClientSecret: String = "clientSecret"
    private var netceteraApiKey: String? = null
    private val prefsName = "HyperswitchPrefs"
    private val keyServerUrl = "server_url"
    private var serverUrl = "http://10.0.2.2:5252"
    private lateinit var editText: EditText

    private fun fetchNetceteraApiKey() {
        reset().get("$serverUrl/netcetera-sdk-api-key").responseString(object : Handler<String?> {
            override fun success(value: String?) {
                try {
                    val result = value?.let { JSONObject(it) }
                    netceteraApiKey = result?.getString("netceteraApiKey")
                } catch (_: Exception) {
                }
            }

            override fun failure(error: FuelError) {}
        })
    }

    private fun getSharedPreferences(): android.content.SharedPreferences {
        return ctx.getSharedPreferences(prefsName, MODE_PRIVATE)
    }

    private fun saveServerUrl(url: String) {
        getSharedPreferences().edit { putString(keyServerUrl, url) }
    }

    private fun loadServerUrl(): String {
        return getSharedPreferences().getString(keyServerUrl, serverUrl) ?: serverUrl
    }

    private fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    private fun updateServerUrl(newUrl: String) {
        if (isValidUrl(newUrl)) {
            serverUrl = newUrl
            saveServerUrl(newUrl)
            setStatus("Reload Client Secret")
        } else {
            setStatus("Invalid URL format")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        ctx = this
        editText = ctx.findViewById(R.id.ipAddressInput)
        serverUrl = loadServerUrl()
        editText.setText(serverUrl)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let { newUrl ->
                    if (newUrl.isNotEmpty()) {
                        updateServerUrl(newUrl)
                    }
                }
            }
        })
        val launchButton = ctx.findViewById<Button>(R.id.launchButton)
        launchButton.setOnClickListener {
            HyperSwitchSDK.shared.presentFragment(this@MainActivity)
        }

    }

    private fun setStatus(error: String) {
        runOnUiThread {
            findViewById<TextView>(R.id.resultText).text = error
        }
    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }
}
