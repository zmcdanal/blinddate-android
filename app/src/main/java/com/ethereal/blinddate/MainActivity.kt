package com.ethereal.blinddate

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ethereal.blinddate.ui.BlindDateApp
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.home.navigation.Home
import com.ethereal.home.navigation.homeGraph

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            BlindDateTheme(darkTheme = true, dynamicColor = false) {
                BlindDateApp()
            }
        }
    }
}