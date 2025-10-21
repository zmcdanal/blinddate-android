package com.ethereal.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ethereal.login.LoginRoute

object Login { const val route = "login" }

fun NavGraphBuilder.loginGraph(nav: NavHostController) {
    composable(Login.route) {
        LoginRoute()
    }
}