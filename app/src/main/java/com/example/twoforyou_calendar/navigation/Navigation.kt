package com.example.twoforyou_calendar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.twoforyou_calendar.screen.home.HomeScreen
import com.example.twoforyou_calendar.screen.splash.SplashScreen

@Composable
fun Navigation(
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    )
    {

        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }

            composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

    }

}