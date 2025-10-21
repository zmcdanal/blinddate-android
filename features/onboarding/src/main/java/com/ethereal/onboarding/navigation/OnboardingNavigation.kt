package com.ethereal.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ethereal.onboarding.OnboardingRoute

object Onboarding {
    const val ROUTE = "onboarding"
}

fun NavGraphBuilder.onboardingGraph(
    onTempContinueToHome: () -> Unit
) {
    composable(Onboarding.ROUTE) {
        OnboardingRoute(onTempContinueToHome = onTempContinueToHome)
    }
}