package com.example.stepcounter.navigation

sealed class AppScreens(val route: String) {
    object Main : AppScreens("main/")
    object Splash : AppScreens("splash/")
    object EditTarget : AppScreens("main/edit-target")
}