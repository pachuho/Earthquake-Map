package com.pachuho.earthquakemap

import androidx.navigation.NamedNavArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Launcher : Screen(
        route =  "launcher"
    )

    data object Map : Screen(
        route = "map"
    )
}