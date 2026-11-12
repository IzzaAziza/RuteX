package com.ibaiqzzaaziza.rutex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ibaiqzzaaziza.rutex.screen.BuatRuteScreen
import com.ibaiqzzaaziza.rutex.screen.DetailDestinasiScreen
import com.ibaiqzzaaziza.rutex.screen.HomeScreen
import com.ibaiqzzaaziza.rutex.screen.LoginScreen
import com.ibaiqzzaaziza.rutex.screen.ProfilScreen
import com.ibaiqzzaaziza.rutex.screen.RegisterScreen
import com.ibaiqzzaaziza.rutex.screen.RiwayatScreen
import com.ibaiqzzaaziza.rutex.screen.RuteAiConstraintScreen
import com.ibaiqzzaaziza.rutex.screen.RuteAiTanpaConstraintScreen
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
        composable(
            route = Screen.RuteManual.route,
            arguments = listOf(
                navArgument(KEY_NAMA_DESTINASI) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { navBackStackEntry ->
            val destinasi = navBackStackEntry.arguments?.getString(KEY_NAMA_DESTINASI)
            RuteManualScreen(navController, destinasi)
        }
        composable(route = Screen.RuteAiConstraint.route) {
            RuteAiConstraintScreen(navController)
        }
        composable(route = Screen.RuteAiTanpaConstraint.route) {
            RuteAiTanpaConstraintScreen(navController)
        }
        composable(route = Screen.Riwayat.route) {
            RiwayatScreen(navController)
        }
        composable(route = Screen.Profil.route) {
            ProfilScreen(navController)
        }
        composable(
            route = Screen.DetailDestinasi.route,
            arguments = listOf(
                navArgument(KEY_ID_DESTINASI) { type = NavType.IntType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt(KEY_ID_DESTINASI) ?: 0
            DetailDestinasiScreen(
                navController = navController,
                id = id,
                onTambahKeRute = { navController.navigate(Screen.RuteManual.withDestinasi(it.name)) }
            )
        }
    }
}
