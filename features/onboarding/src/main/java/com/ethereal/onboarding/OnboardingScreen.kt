package com.ethereal.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun OnboardingRoute(viewModel: OnboardingViewModel = hiltViewModel()) {
}

@Composable
internal fun OnboardingScreen() {

}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun PreviewOnboardingScreen() {
    OnboardingRoute()
}