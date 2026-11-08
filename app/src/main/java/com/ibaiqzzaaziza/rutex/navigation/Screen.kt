package com.ibaiqzzaaziza.rutex.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splashScreen")
    data object Home : Screen("homeScreen")
}
