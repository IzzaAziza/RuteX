package com.ibaiqzzaaziza.rutex

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ibaiqzzaaziza.rutex.navigation.SetupNavGraph
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Desain RuteX berlatar terang, jadi ikon status bar dibuat gelap
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            RuteXTheme {
                SetupNavGraph()
            }
        }
    }
}
