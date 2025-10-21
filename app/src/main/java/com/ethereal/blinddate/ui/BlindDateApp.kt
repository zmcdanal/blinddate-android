package com.ethereal.blinddate.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ethereal.home.HomeRoute
import com.ethereal.home.navigation.Home
import com.ethereal.login.LoginRoute
import com.ethereal.login.navigation.Login
import com.ethereal.ui.BlindDateBackground
import com.ethereal.ui.BlindDateBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlindDateApp() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryFlow.collectAsState(initial = nav.currentBackStackEntry)
    val route = backStack?.destination?.route.orEmpty()

    val showHomeBar = route.startsWith(Home.route)
    val showMapBar = false //route.startsWith(Map.route)
    val showBottomBar = showHomeBar || showMapBar || !route.startsWith(Login.route)
    val showTopBack = false //route.startsWith(Questionnaire.route)

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
                        // Home screen bottom bar: History – Home – Profile
                        BlindDateBottomBar(
                            onHistory = { },
                            onHome = { nav.navigate(Home.route) { popUpTo(0) } },
                            onProfile = { }
                        )
                    } else if (showMapBar) {
                        // Map screen bottom bar: Cancel Trip – Home – Open in Maps
                        //TODO
//                    BlindDateMapBar(
//                        onCancel = { nav.popBackStack(Home.route, inclusive = false) },
//                        onHome   = { nav.navigate(Home.route) { popUpTo(0) } },
//                        onOpenMaps = { /* trigger external maps intent */ }
//                    )
                    }
                }
            }
        ) { pad ->
            NavHost(
                navController = nav,
                startDestination = Login.route,
                modifier = Modifier.padding(pad)
            ) {
                composable(Home.route) {
                    HomeRoute()
                    // HomeRoute(
//                    onStartQuestionnaire = { nav.navigate(Questionnaire.route) },
//                    onHistory = { nav.navigate(History.route) },
//                    onProfile = { nav.navigate(Profile.route) }
                    // )
                }
                composable(Login.route) {
                    LoginRoute()
                }
//            composable(Questionnaire.route) {
//                QuestionnaireScreen(
//                    onFinished = { nav.navigate(Map.route) }
//                )
//            }
//            composable(Map.route) {
//                MapScreen(
//                    onOpenExternalMaps = { /* same action as bar’s Open in Maps */ }
//                )
//            }
//            composable(History.route) { HistoryScreen() }
//            composable(Profile.route) { ProfileScreen() }
            }
        }
    }
}