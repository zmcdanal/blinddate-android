package com.ethereal.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ethereal.onboarding.OnboardingRoute

object Onboarding {
    const val ROUTE = "onboarding"
}

object OnbIntro {
    const val ROUTE = "onboarding/intro"
}

object OnbAccount {
    const val ROUTE = "onboarding/account"
}

object OnbPartner {
    const val ROUTE = "onboarding/partner"
}

object OnbLocation {
    const val ROUTE = "onboarding/location"
}


fun NavGraphBuilder.onboardingGraph(
    nav: NavHostController,
    onFinished: () -> Unit
) {
    navigation(
        startDestination = OnbIntro.ROUTE,
        route = Onboarding.ROUTE
    ) {
        composable(OnbIntro.ROUTE) {
            OnboardingRoute(
                step = 1, totalSteps = 4,
                onAdvance = { nav.navigate(OnbAccount.ROUTE) },
                onFinish = {},
                onBack = {}
            )
        }
        composable(OnbAccount.ROUTE) {
            OnboardingRoute(
                step = 2, totalSteps = 4,
                onAdvance = { nav.navigate(OnbPartner.ROUTE) },
                onBack = { nav.popBackStack() },
                onFinish = {}
            )
        }
        composable(OnbPartner.ROUTE) {
            OnboardingRoute(
                step = 3, totalSteps = 4,
                onFinish = { },
                onBack = { },
                onAdvance = { nav.navigate(OnbLocation.ROUTE) }
            )
        }
        composable(OnbLocation.ROUTE) {
            OnboardingRoute(
                step = 4, totalSteps = 4,
                onAdvance = onFinished,
                onFinish = onFinished,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
