package com.ethereal.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ethereal.home.HomeRoute

object Home { const val route = "home" }

fun NavGraphBuilder.homeGraph(nav: NavHostController) {
    composable(Home.route) {
        HomeRoute()
    }
}
