package com.ibaiqzzaaziza.rutex.navigation

const val KEY_PAKAI_CONSTRAINT = "pakaiConstraint"

sealed class Screen(val route: String) {
    data object Splash : Screen("splashScreen")
    data object Login : Screen("loginScreen")
    data object Register : Screen("registerScreen")
    data object Home : Screen("homeScreen")
    data object BuatRute : Screen("buatRuteScreen")
    data object RuteManual : Screen("ruteManualScreen")

    // Form AI dipakai oleh dua mode, jadi mode-nya dikirim lewat argumen navigasi
    data object RuteAi : Screen("ruteAiScreen/{$KEY_PAKAI_CONSTRAINT}") {
        fun withMode(pakaiConstraint: Boolean) = "ruteAiScreen/$pakaiConstraint"
    }
}
