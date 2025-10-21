package com.ethereal.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ethereal.home.HomeRoute

object Home {
    const val ROUTE = "home"
}

fun NavGraphBuilder.homeGraph() {
    composable(Home.ROUTE) {
        HomeRoute()
    }
}
