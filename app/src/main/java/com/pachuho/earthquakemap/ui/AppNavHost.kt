package com.pachuho.earthquakemap.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pachuho.earthquakemap.ui.screen.map.MapRoute
import com.pachuho.earthquakemap.ui.screen.table.TableRoute
import com.pachuho.earthquakemap.ui.util.NavigationAnimation
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = Screen.Map.route
        ) {
            NavigationAnimation.apply {
                composable(
                    route = Screen.Map.route,
                    enterTransition = enterTransition,
                    exitTransition = exitTransition,
                    popEnterTransition = popEnterTransition,
                    popExitTransition = popExitTransition
                ) {
                    MapRoute(
                        onSnackBar = { message ->
                            scope.launch { snackbarHostState.showSnackbar(message) }
                        }
                    ) {
                        navController.navigate(Screen.Table.route)
                    }
                }
                composable(
                    route = Screen.Table.route,
                    enterTransition = enterTransition,
                    exitTransition = exitTransition,
                    popEnterTransition = popEnterTransition,
                    popExitTransition = popExitTransition
                ) {
                    TableRoute(
                        onSnackBar = { message ->
                            scope.launch { snackbarHostState.showSnackbar(message) }
                        }
                    )
                }

                composable(
                    route = Screen.Setting.route
                ) {
                }
            }
        }
    }

}