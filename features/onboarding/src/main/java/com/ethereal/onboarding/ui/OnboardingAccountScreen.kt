package com.ethereal.onboarding.ui

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
import com.ethereal.onboarding.OnboardingUiState
import com.ethereal.onboarding.ui.components.GoogleSignUpButton
import com.ethereal.onboarding.ui.components.TermsRow
import com.ethereal.ui.BlindDateBackground
import reusables.EmailField
import reusables.PasswordField

@Composable
fun OnboardingAccountScreen(
    uiState: OnboardingUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onTermsChange: (Boolean) -> Unit,
    onOpenTermsOfService: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    onSignUpWithGoogle: () -> Unit,
    onCreateAccount: () -> Unit
) {

    Spacer(Modifier.height(12.dp))

    GoogleSignUpButton(
        loading = uiState.isLoading,                  // or separate googleLoading flag if you want finer control
        onClick = onSignUpWithGoogle
    )

    Spacer(Modifier.height(24.dp))

    // Divider
    Row(
        modifier = Modifier.fillMaxWidth(0.85f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
        Text(
            " OR ",
            style = MaterialTheme.typography.labelMedium,
            color = FogWhite.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
    }

    Spacer(Modifier.height(24.dp))
    // Email
    EmailField(
        value = uiState.emailAddress,
        onValueChange = onEmailChange,
        isError = uiState.emailAddress.isNotEmpty() && !uiState.isEmailValid,
        errorText = "Enter a valid email.",
        modifier = Modifier.padding(bottom = 12.dp)
    )

    // Password
    PasswordField(
        value = uiState.password,
        onValueChange = onPasswordChange,
        isError = uiState.password.isNotEmpty() && !uiState.isPasswordValid,
        errorText = "Password must be at least 6 characters.",
        modifier = Modifier.padding(bottom = 12.dp)
    )

    // Confirm Password
    PasswordField(
        placeholder = "Confirm Password",
        value = uiState.confirmPassword,
        onValueChange = onConfirmChange,
        isError = uiState.confirmPassword.isNotEmpty() && !uiState.isConfirmPasswordValid,
        errorText = "Passwords don’t match.",
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Spacer(Modifier.height(12.dp))

    TermsRow(
        checked = uiState.termsAccepted,
        onCheckedChange = onTermsChange,
        onOpenTermsOfService = onOpenTermsOfService,
        onOpenPrivacyPolicy = onOpenPrivacyPolicy
    )

    Spacer(Modifier.height(20.dp))
    Button(
        onClick = onCreateAccount,
        enabled = uiState.canSubmitForm,
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonRose,
            contentColor = FogWhite
        ),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(56.dp)
    ) { Text("Create account") }

}

@Preview
@Composable
fun PreviewOnboardingAccountScreen() {
    BlindDateTheme {
        BlindDateBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnboardingAccountScreen(
                    uiState = OnboardingUiState(
                        emailAddress = "",
                        password = "",
                        confirmPassword = "",
                        termsAccepted = false
                    ),
                    onEmailChange = {},
                    onPasswordChange = {},
                    onConfirmChange = {},
                    onTermsChange = {},
                    onOpenTermsOfService = {},
                    onOpenPrivacyPolicy = {},
                    onCreateAccount = {},
                    onSignUpWithGoogle = {},
                )
            }
        }
    }
}