package com.ethereal.blinddate.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import com.ethereal.blinddate.navigation.Entry
import com.ethereal.blinddate.navigation.entryGraph
import com.ethereal.common.navigation.ui.ActionBottomBar
import com.ethereal.common.navigation.ui.BottomTabsBar
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
    // TODO restructure bottom bar logic
    val hideBottomBar = route.isEmpty() ||
            route.startsWith(Login.ROUTE) ||
            route.startsWith(Onboarding.ROUTE) ||
            route.startsWith(Entry.ROUTE)

    val showBottomBar = !hideBottomBar && route.startsWith(Home.ROUTE)

    val showCenterAction = true

    fun navigateSingleTop(route: String) {
        nav.navigate(route) {
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
            topBar = { },

            floatingActionButton = {
                if (showBottomBar && showCenterAction) {
                    FloatingActionButton(
                        onClick = { }
                    ) {
                        Icon(Icons.Outlined.Star, contentDescription = "Do the thing")
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                if (showBottomBar) {
                    if (showCenterAction) {
                        ActionBottomBar(
                            currentRoute = route,
                            onNavigate = ::navigateSingleTop,
                        )
                    } else {
                        BottomTabsBar(
                            currentRoute = route,
                            onNavigate = ::navigateSingleTop
                        )
                    }
                }
            }
        ) { pad ->
            NavHost(
                navController = nav,
                startDestination = Entry.ROUTE,
                modifier = Modifier.padding(pad)
            ) {

                entryGraph(
                    onToHome = { navigateSingleTop(Home.ROUTE) },
                    onToLogin = { nav.navigate(Login.ROUTE) { popUpTo(0) } }
                )

                loginGraph(
                    onLoggedIn = { navigateSingleTop(Home.ROUTE) },
                    onSignUp = { nav.navigate(Onboarding.ROUTE) }
                )
                onboardingGraph(nav, onFinished = {
                    navigateSingleTop(Home.ROUTE)
                })
                homeGraph()

            }
        }
    }
}
