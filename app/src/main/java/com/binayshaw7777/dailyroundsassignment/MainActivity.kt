package com.binayshaw7777.dailyroundsassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.binayshaw7777.dailyroundsassignment.ui.navigation.AppNavigation
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyRoundsAssignmentTheme {
                AppNavigation()
            }
        }
    }
}
