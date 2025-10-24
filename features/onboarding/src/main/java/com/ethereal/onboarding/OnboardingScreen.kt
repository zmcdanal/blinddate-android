package com.ethereal.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
import com.ethereal.onboarding.ui.OnboardingAccountScreen
import com.ethereal.onboarding.ui.OnboardingIntroScreen
import com.ethereal.onboarding.ui.OnboardingUserInfoScreen

@Composable
fun OnboardingRoute(
    viewModel: OnboardingViewModel = hiltViewModel(),
    step: Int,
    totalSteps: Int,
    onAdvance: () -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {

    val uistate by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is OnboardingAccountEvent.NavigateToNextStep -> onAdvance()
                is OnboardingAccountEvent.ShowSnackbar -> {}
                is OnboardingAccountEvent.OpenTermsOfService -> { /* TODO: open TOS */
                }

                is OnboardingAccountEvent.OpenPrivacyPolicy -> { /* TODO: open Privacy */
                }
            }
        }
    }

    OnboardingScreen(
        uiState = uistate,
        step = step,
        totalSteps = totalSteps,
        onAdvance = onAdvance,
        onFinish = onFinish,
        onBack = onBack,
        onTextFieldChange = viewModel::updateTextField,
        onTermsChange = viewModel::updateToggle,
        onHasPartnerChange = viewModel::updateHasPartnerToggle,
        onRadiusChange = viewModel::updateRadius,
        onEnableLocation = viewModel::requestLocationPermission,
        onOpenTermsOfService = viewModel::openTermsOfService,
        onOpenPrivacyPolicy = viewModel::openPrivacyPolicy,
        onSignUpWithGoogle = viewModel::submitSignInWithGoogle,
        onCreateAccount = viewModel::submitCreateAccount
    )

}

@Composable
internal fun OnboardingScreen(
    uiState: OnboardingUiState,
    onTextFieldChange: (TextFieldType, String) -> Unit,
    onTermsChange: (OnboardingAccountToggle, Boolean) -> Unit,
    onHasPartnerChange: (Boolean) -> Unit,
    onRadiusChange: (Int) -> Unit,
    onEnableLocation: () -> Unit,
    onOpenTermsOfService: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    step: Int,
    totalSteps: Int,
    onSignUpWithGoogle: () -> Unit,
    onCreateAccount: () -> Unit,
    onAdvance: () -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top row: back/skip + step indicator
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            if (step < 3) {
                TextButton(onClick = onBack) { Text("Back", color = FogWhite) }
            } else {
                Spacer(Modifier.width(64.dp))
            }
            Text("Step $step of $totalSteps", color = FogWhite)
        }

        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { step / totalSteps.toFloat() },
            modifier = Modifier.fillMaxWidth(),
            color = NeonRose,
            trackColor = FogWhite.copy(alpha = .15f),
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )

        Spacer(Modifier.height(24.dp))

        BackHandler(enabled = (step == 3 && uiState.accountCreated)) {
            // do nothing: swallow back press
        }

        when (step) {
            1 -> {
                OnboardingIntroScreen(
                    onAdvance = onAdvance
                )
            }

            2 -> {
                OnboardingAccountScreen(
                    uiState = uiState,
                    onEmailChange = {
                        onTextFieldChange(
                            TextFieldType.EMAIL,
                            it

                        )
                    },
                    onPasswordChange = {
                        onTextFieldChange(
                            TextFieldType.PASSWORD,
                            it
                        )
                    },
                    onConfirmChange = {
                        onTextFieldChange(
                            TextFieldType.CONFIRM_PASSWORD,
                            it
                        )
                    },
                    onTermsChange = {
                        onTermsChange(OnboardingAccountToggle.TERMS, it)
                    },
                    onSignUpWithGoogle = onSignUpWithGoogle,
                    onCreateAccount = onCreateAccount,
                    onOpenTermsOfService = onOpenTermsOfService,
                    onOpenPrivacyPolicy = onOpenPrivacyPolicy,
                )
            }

            3 -> {
                OnboardingUserInfoScreen(
                    isPartner = uiState.hasPartner,
                    onPartnerToggleChange = onHasPartnerChange,
                    displayName = uiState.userName,
                    onDisplayNameChange = { onTextFieldChange(TextFieldType.USER, it) },
                    partnerName = uiState.partnerName ?: "",
                    onPartnerNameChange = { onTextFieldChange(TextFieldType.PARTNER, it) },
                    defaultRadius = uiState.defaultRadius,
                    onDefaultRadiusChange = onRadiusChange,
                    locationEnabled = uiState.locationEnabled,
                    onLocationEnabledChange = onEnableLocation,
                    onFinish = onFinish
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun PreviewOnboardingScreen() {
    OnboardingRoute(
        step = 1,
        totalSteps = 3,
        onAdvance = {},
        onFinish = {},
        onBack = {},
    )
}