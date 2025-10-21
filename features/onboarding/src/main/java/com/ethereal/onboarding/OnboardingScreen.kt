package com.ethereal.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
import com.ethereal.onboarding.ui.OnboardingAccountScreen
import com.ethereal.onboarding.ui.OnboardingIntroScreen
import com.ethereal.onboarding.ui.OnboardingUserInfoScreen
import reusables.PasswordField
import reusables.EmailField
import reusables.NameField

@Composable
fun OnboardingRoute(
    viewModel: OnboardingViewModel = hiltViewModel(),
    step: Int,
    totalSteps: Int,
    onAdvance: () -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    OnboardingScreen(
        viewModel = viewModel,
        step = step,
        totalSteps = totalSteps,
        onAdvance = onAdvance,
        onFinish = onFinish,
        onBack = onBack,
    )
}

@Composable
internal fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    step: Int,
    totalSteps: Int,
    onAdvance: () -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    // Temporary while I adjust Ui - UiState will eventually take over
    var isPartner by viewModel.isPartner
    var displayName by viewModel.displayName
    var partnerName by viewModel.partnerName
    var email by viewModel.email
    var password by viewModel.password
    var confirm by viewModel.confirm
    var termsAccepted by viewModel.termsAccepted
    var defaultRadius by viewModel.defaultRadius
    var locationEnabled by viewModel.locationEnabled
    var accountCreated by viewModel.accountCreated

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

        BackHandler(enabled = (step == 3 && accountCreated)) {
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
                    emailValue = email,
                    onEmailChange = { email = it },
                    passwordValue = password,
                    onPasswordChange = { password = it },
                    confirmValue = confirm,
                    onConfirmChange = { confirm = it },
                    termsAccepted = termsAccepted,
                    onTermsChange = { termsAccepted = it },
                    onAdvance = onAdvance
                )
            }

            3 -> {
                OnboardingUserInfoScreen(
                    isPartner = isPartner,
                    onPartnerToggleChange = { isPartner = it },
                    displayName = displayName,
                    onDisplayNameChange = { displayName = it },
                    partnerName = partnerName,
                    onPartnerNameChange = { partnerName = it },
                    defaultRadius = defaultRadius,
                    onDefaultRadiusChange = { defaultRadius = it },
                    locationEnabled = locationEnabled,
                    onLocationEnabledChange = { locationEnabled = it },
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