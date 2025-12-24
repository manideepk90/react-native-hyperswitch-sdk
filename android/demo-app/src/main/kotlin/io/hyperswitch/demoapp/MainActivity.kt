package io.hyperswitch.demoapp

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.AutoCompleteTextView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import io.hyperswitch.HyperSwitchSDK
import org.json.JSONObject

class MainActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {
    lateinit var ctx: Activity
    private lateinit var apiKeyInput: EditText
    private lateinit var environmentSpinner: Spinner
    private lateinit var environment1Spinner : Spinner
    private lateinit var timeoutInput: EditText
    private lateinit var debugSwitch: Switch
    private lateinit var backendUrlInput: EditText
    private lateinit var jsonInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        ctx = this

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        apiKeyInput = findViewById(R.id.apiKeyInput)
        environmentSpinner = findViewById(R.id.environmentSpinner)
        timeoutInput = findViewById(R.id.timeoutInput)
        debugSwitch = findViewById(R.id.debugSwitch)
        backendUrlInput = findViewById(R.id.backendUrlInput)
        jsonInput = findViewById(R.id.jsonInput)
        environment1Spinner = findViewById(R.id.environmentSpinner1)
        // Setup Spinner
        val environments = arrayOf("Production", "Sandbox")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, environments)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        environmentSpinner.adapter = adapter

        val items = arrayOf("FlowBird", "Sungroup")

        val adapter1 = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            items
        )

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        environment1Spinner.adapter = adapter1

        backendUrlInput.setText("http://10.0.2.2:5252")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_open) {
            handleOpen()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleOpen() {
        val apiKey = apiKeyInput.text.toString()
        val environment = environmentSpinner.selectedItem.toString()
        val timeout = timeoutInput.text.toString().toIntOrNull() ?: 5000
        val debugMode = debugSwitch.isChecked
        val backendUrl = backendUrlInput.text.toString()
        val jsonProps = jsonInput.text.toString()

        val props = mutableMapOf<String, Any>(
            "publishableKey" to apiKey,
            "environment" to environment.lowercase(),
            "timeout" to timeout,
            "debugMode" to debugMode,
            "backendUrl" to backendUrl,
            "type" to "payment",
            "from" to "rn"
        )

        try {
            if (jsonProps.isNotEmpty()) {
                val jsonObject = JSONObject(jsonProps)
                val keys = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = jsonObject.get(key)
                    if (value is String) props[key] = value
                    else if (value is Int) props[key] = value
                    else if (value is Boolean) props[key] = value
                    else if (value is Double) props[key] = value
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid JSON Properties", Toast.LENGTH_SHORT).show()
            return
        }

        HyperSwitchSDK.shared.presentFragment(this@MainActivity, props)
    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }
}
