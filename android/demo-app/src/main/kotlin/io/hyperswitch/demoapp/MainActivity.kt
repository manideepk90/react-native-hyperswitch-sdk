package io.hyperswitch.demoapp

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import io.hyperswitch.HyperSwitchSDK
import org.json.JSONObject
import android.util.Patterns
import androidx.core.content.edit
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.github.kittinunf.fuel.Fuel.reset
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import io.hyperswitch.paymentsheet.AddressDetails
import io.hyperswitch.paymentsheet.PaymentSheet
import io.hyperswitch.paymentsheet.PaymentSheetResult
import androidx.core.graphics.toColorInt
import io.hyperswitch.PaymentSession

class MainActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {
    lateinit var ctx: Activity
    private lateinit var backendUrlInput: EditText
    private lateinit var jsonInput: EditText
    private var serverURL: String = "http://10.0.2.2:5252"
    private val keyServerUrl = "server_url"
    private val prefsName = "HyperswitchPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        ctx = this
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        backendUrlInput = findViewById(R.id.backendUrlInput)
        jsonInput = findViewById(R.id.jsonInput)
        serverURL = loadServerUrl()
        backendUrlInput.setText(serverURL)
        backendUrlInput.addTextChangedListener(object : TextWatcher {
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
    }

    private fun getSharedPreferences(): android.content.SharedPreferences {
        return ctx.getSharedPreferences(prefsName, MODE_PRIVATE)
    }

    private fun saveServerUrl(url: String) {
        getSharedPreferences().edit { putString(keyServerUrl, url) }
    }

    private fun loadServerUrl(): String {
        return getSharedPreferences().getString(keyServerUrl, serverURL) ?: serverURL
    }

    private fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    private fun updateServerUrl(newUrl: String) {
        if (isValidUrl(newUrl)) {
            serverURL = newUrl
            saveServerUrl(newUrl)
            //TODO message
        } else {
            //TODO message
        }
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
    private fun getCustomisations(): PaymentSheet.Configuration {
        /**
         *
         * Customisations
         *
         * */

        val primaryButtonShape = PaymentSheet.PrimaryButtonShape(32f, 0f)
        val address =
            PaymentSheet.Address.Builder().city("city").country("US").line1("US").line2("line2")
                .postalCode("560060").state("California").build()
        val billingDetails: PaymentSheet.BillingDetails =
            PaymentSheet.BillingDetails.Builder().address(address).email("email.com")
                .name("John Doe").phone("1234123443").build()
        val shippingDetails = AddressDetails("Shipping Inc.", address, "6205007614", true)

        val primaryButton = PaymentSheet.PrimaryButton(
            shape = primaryButtonShape,
        )
        val color1: PaymentSheet.Colors = PaymentSheet.Colors(
            primary = "#8DBD00".toColorInt(),
            surface = "#F5F8F9".toColorInt(),
        )

        val color2: PaymentSheet.Colors = PaymentSheet.Colors(
            primary = "#8DBD00".toColorInt(),
            surface = "#F5F8F9".toColorInt(),
        )

        val appearance: PaymentSheet.Appearance = PaymentSheet.Appearance(
            typography = PaymentSheet.Typography(
                sizeScaleFactor = 1f, fontResId = R.font.montserrat
            ),
            primaryButton = primaryButton,
            colorsLight = color1,
            colorsDark = color2,
            theme = PaymentSheet.Theme.Light
        )

        val configuration =
            PaymentSheet.Configuration.Builder("Example, Inc.")
                //.appearance(appearance)
                .defaultBillingDetails(billingDetails).primaryButtonLabel("Purchase ($2.00)")
                .paymentSheetHeaderLabel("Select payment method")
                .savedPaymentSheetHeaderLabel("Payment methods").shippingDetails(shippingDetails)
                .allowsPaymentMethodsRequiringShippingAddress(false)
                .allowsDelayedPaymentMethods(true).displaySavedPaymentMethodsCheckbox(true)
                .displaySavedPaymentMethods(true).disableBranding(true).showVersionInfo(true)


        return configuration.build()
    }


    private fun openSDK(publishableKey: String, clientSecret: String) {
        val paymentSession = PaymentSession(ctx, publishableKey)
        paymentSession.initPaymentSession(clientSecret)
        val customisations = getCustomisations()
        Log.i("Manideep", "$publishableKey, $clientSecret")
        paymentSession.presentPaymentSheet(customisations, ::onPaymentSheetResult)
    }

    private fun getPaymentIntent(props: JSONObject) {
        try {
            FuelManager.instance.reset()
            val url = "$serverURL/create-payment-intent"
            val jsonBody = """$props"""
            val headers = mapOf("Content-Type" to "application/json")
            Fuel.post(url)
                .body(jsonBody)
                .header(headers)
                .responseString(
                    object : Handler<String?> {
                        override fun failure(error: FuelError) {
                            //TODO
                        }

                        override fun success(value: String?) {
                            try {
                                Log.i("Manideep", "here ${value}")
                                val result = value?.let { JSONObject(it) }
                                if (result != null) {
                                    openSDK(
                                        result.getString("publishableKey"),
                                        result.getString("clientSecret")
                                    )
                                }else{
                                    //TODO
                                }
                            } catch (e: Exception) {
                                //TODO
                            }


                        }
                    }
                )
        }catch(e:Exception){

        }

    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                setStatus(paymentSheetResult.data)
            }

            is PaymentSheetResult.Failed -> {
                setStatus(paymentSheetResult.error.message ?: "")
            }

            is PaymentSheetResult.Completed -> {
                setStatus(paymentSheetResult.data)
            }
        }
    }

    private fun handleOpen() {
        val jsonProps = jsonInput.text.toString()
        try {
            if (jsonProps.isNotEmpty()) {
                val jsonObject = JSONObject(jsonProps)
                val keys = jsonObject.keys()
                getPaymentIntent(jsonObject)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid JSON Properties", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun setStatus(data: String){
        Toast.makeText(this,data.toString() , Toast.LENGTH_SHORT).show()

    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }
}
