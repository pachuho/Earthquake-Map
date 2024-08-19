package com.pachuho.earthquakemap

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pachuho.earthquakemap.ui.screen.launcher.LauncherScreen
import com.pachuho.earthquakemap.ui.screen.map.MapScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Launcher.route
    ) {
        composable(
            route = Screen.Launcher.route
        ) {
            LauncherScreen {
                navController.navigate(Screen.Map.route)
            }
        }

        composable(
            route = Screen.Map.route
        ) {
            MapScreen()
        }
    }
}