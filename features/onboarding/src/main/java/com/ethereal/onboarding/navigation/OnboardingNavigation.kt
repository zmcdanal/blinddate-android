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


// com.ethereal.onboarding.navigation.OnboardingNav.kt

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
                step = 1, totalSteps = 3,
                onAdvance = { nav.navigate(OnbAccount.ROUTE) },
                onFinish = {},
                onBack = {}
            )
        }
        composable(OnbAccount.ROUTE) {
            OnboardingRoute(
                step = 2, totalSteps = 3,
                onAdvance = { nav.navigate(OnbPartner.ROUTE) },
                onBack = { nav.popBackStack() },
                onFinish = {}
            )
        }
        composable(OnbPartner.ROUTE) {
            OnboardingRoute(
                step = 3, totalSteps = 3,
                onFinish = { onFinished() },
                onBack = { nav.popBackStack() },
                onAdvance = {}
            )
        }
    }
}
