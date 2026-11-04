package com.ibaiqzzaaziza.rutex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ibaiqzzaaziza.rutex.ui.screen.HomeScreen
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RuteXTheme {
                HomeScreen()
            }
        }
    }
}