package com.ethereal.blinddate.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ethereal.home.navigation.Home
import com.ethereal.home.navigation.homeGraph
import com.ethereal.login.navigation.Login
import com.ethereal.login.navigation.loginGraph
import com.ethereal.onboarding.navigation.Onboarding
import com.ethereal.onboarding.navigation.onboardingGraph
import com.ethereal.ui.BlindDateBackground
import com.ethereal.ui.BlindDateBottomBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlindDateApp() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryFlow.collectAsState(initial = nav.currentBackStackEntry)
    val route = backStack?.destination?.route.orEmpty()

    val showHomeBar = route.startsWith(Home.ROUTE)
    val showMapBar = false // route.startsWith(Map.route)
    val showBottomBar = showHomeBar || showMapBar || !route.startsWith(Login.ROUTE)
    val showTopBack = false // route.startsWith(Questionnaire.route)

    fun navigateHome() {
        nav.navigate(Home.ROUTE) {
            popUpTo(nav.graph.findStartDestination().id) {
                inclusive = false
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BlindDateBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                if (showTopBack) {
                    TopAppBar(
                        navigationIcon = {
                            IconButton({ nav.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        title = {}
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    if (showHomeBar) {
                        BlindDateBottomBar(
                            onHistory = { /* TODO */ },
                            onHome = { navigateHome() },
                            onProfile = { /* TODO */ }
                        )
                    } else if (showMapBar) {
                        // TODO map bar variant
                    }
                }
            }
        ) { pad ->
            NavHost(
                navController = nav,
                startDestination = Login.ROUTE,
                modifier = Modifier.padding(pad)
            ) {

                loginGraph(
                    onLoggedIn = { navigateHome() },
                    onSignUp = { nav.navigate(Onboarding.ROUTE) }
                )
                onboardingGraph(
                    onTempContinueToHome = { navigateHome() }
                )
                homeGraph()

                // questionnaireGraph(nav), mapGraph(nav)
            }
        }
    }
}
