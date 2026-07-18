package com.binayshaw7777.dailyroundsassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.binayshaw7777.dailyroundsassignment.ui.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-Activity entry point for the application.
 *
 * Annotated with [AndroidEntryPoint] to enable Hilt injection in this Activity.
 * Enables edge-to-edge rendering and sets the Compose content to the root
 * navigation graph [AppNavigation].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}
