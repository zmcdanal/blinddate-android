package com.ethereal.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ethereal.login.LoginRoute

object Login {
    const val ROUTE = "login"
}

fun NavGraphBuilder.loginGraph(
    onLoggedIn: () -> Unit,
    onSignUp: () -> Unit
) {
    composable(Login.ROUTE) {
        LoginRoute(
            onLoggedIn = onLoggedIn,
            onSignUp = onSignUp
        )
    }
}