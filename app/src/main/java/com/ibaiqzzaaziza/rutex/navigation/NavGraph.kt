package com.ibaiqzzaaziza.rutex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ibaiqzzaaziza.rutex.screen.BuatRuteScreen
import com.ibaiqzzaaziza.rutex.screen.HomeScreen
import com.ibaiqzzaaziza.rutex.screen.LoginScreen
import com.ibaiqzzaaziza.rutex.screen.RegisterScreen
import com.ibaiqzzaaziza.rutex.screen.RuteAiScreen
import com.ibaiqzzaaziza.rutex.screen.RuteManualScreen
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
        composable(route = Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.BuatRute.route) {
            BuatRuteScreen(navController)
        }
        composable(route = Screen.RuteManual.route) {
            RuteManualScreen(navController)
        }
        composable(
            route = Screen.RuteAi.route,
            arguments = listOf(
                navArgument(KEY_PAKAI_CONSTRAINT) { type = NavType.BoolType }
            )
        ) { navBackStackEntry ->
            val pakaiConstraint = navBackStackEntry.arguments?.getBoolean(KEY_PAKAI_CONSTRAINT) ?: true
            RuteAiScreen(navController, pakaiConstraint)
        }
    }
}
