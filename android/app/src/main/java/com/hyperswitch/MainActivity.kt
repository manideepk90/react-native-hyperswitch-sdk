package com.hyperswitch

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }

//  override fun getMainComponentName(): String = "hyperswitch"

    /**
     * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
     * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
     */
//  override fun createReactActivityDelegate(): ReactActivityDelegate =
//      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Set up the NavHost for navigation
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home",
                    ) {
                        composable("home") {
                            HomeScreen(
                                onStartHyperswitchFragment = {

                                    val reactFragment =
                                        HyperFragment.Builder().setComponentName("hyperswitch")
                                            .build()
                                    val fragmentManager = activity.supportFragmentManager
                                    val fragmentTransaction: FragmentTransaction =
                                        fragmentManager.beginTransaction()
                                    fragmentTransaction.add(
                                        android.R.id.content,
                                        reactFragment,
                                        "HyperPaymentSheet"
                                    )
                                    fragmentTransaction.addToBackStack("HyperPaymentSheet")
                                    fragmentTransaction.commit()
                                },
                            )
                        }

                        composable("reactNative") {
                            ReactNativeScreen()
                        }
                    }
                }
            }
        }

    }


    @Composable
    fun HomeScreen(
        onStartHyperswitchFragment: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "HyperSwitch SDK",
                fontSize = 30.sp,
                modifier = Modifier.padding(15.dp),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onStartHyperswitchFragment,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("open React Native Fragment")
            }
        }
    }

    @Composable
    fun ReactNativeScreen() {

    }
}
