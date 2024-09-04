package com.pachuho.earthquakemap.ui

sealed class Screen(
    val route: String
) {
    data object Table : Screen(
        route =  "table"
    )

    data object Map : Screen(
        route = "map"
    )

    data object Setting : Screen(
        route =  "setting"
    )
}