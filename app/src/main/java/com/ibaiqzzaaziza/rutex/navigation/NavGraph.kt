package com.ibaiqzzaaziza.rutex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ibaiqzzaaziza.rutex.screen.HomeScreen
import com.ibaiqzzaaziza.rutex.screen.LoginScreen
import com.ibaiqzzaaziza.rutex.screen.SplashScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
    }
}
