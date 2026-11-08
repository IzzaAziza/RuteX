package com.ibaiqzzaaziza.rutex.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("homeScreen")
}
