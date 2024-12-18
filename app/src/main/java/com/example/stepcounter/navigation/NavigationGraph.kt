package com.example.stepcounter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stepcounter.presentation.edittarget.EditTargetScreen
import com.example.stepcounter.presentation.splash.SplashScreen
import com.example.stepcounter.presentation.stepsscreen.MainScreen


@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash.route
    ) {
        composable(
            route = AppScreens.Main.route
        ) {
            MainScreen(navController)
        }
        composable(
            route = AppScreens.Splash.route
        ) {
            SplashScreen(navController)
        }
        composable(
            route = AppScreens.EditTarget.route
        ) {
            EditTargetScreen{navController.navigateUp()}
        }
    }
}