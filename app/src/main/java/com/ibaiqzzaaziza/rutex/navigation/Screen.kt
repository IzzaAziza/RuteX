package com.ibaiqzzaaziza.rutex.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splashScreen")
    data object Login : Screen("loginScreen")
    data object Register : Screen("registerScreen")
    data object Home : Screen("homeScreen")
    data object BuatRute : Screen("buatRuteScreen")
    data object RuteManual : Screen("ruteManualScreen")
    data object RuteAiConstraint : Screen("ruteAiConstraintScreen")
    data object RuteAiTanpaConstraint : Screen("ruteAiTanpaConstraintScreen")
    data object Riwayat : Screen("riwayatScreen")
}
