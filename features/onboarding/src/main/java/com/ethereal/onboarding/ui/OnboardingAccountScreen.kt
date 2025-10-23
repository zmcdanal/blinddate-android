package com.ethereal.onboarding.ui

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.ethereal.onboarding.ui.components.TermsRow
import com.ethereal.ui.BlindDateBackground
import reusables.EmailField
import reusables.PasswordField

@Composable
fun OnboardingAccountScreen(
    emailValue: String,
    onEmailChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    confirmValue: String,
    onConfirmChange: (String) -> Unit,
    termsAccepted: Boolean,
    onTermsChange: (Boolean) -> Unit,
    onOpenTermsOfService: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    onAdvance: () -> Unit
) {
    // Account setup: Email / Password / Confirm / Terms
    EmailField(
        value = emailValue,
        onValueChange = onEmailChange,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    PasswordField(
        value = passwordValue,
        onValueChange = onPasswordChange,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    PasswordField(
        placeholder = "Confirm Password",
        value = confirmValue,
        onValueChange = onConfirmChange,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    // TODO - Move to viewModel
    val emailOk = Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()
    val passwordOk = passwordValue.length >= 6
    val confirmOk = confirmValue == passwordValue
    val canSubmit = emailOk && passwordOk && confirmOk && termsAccepted

    if (!emailOk && emailValue.isNotEmpty()) {
        Text(
            "Enter a valid email.",
            color = Color.Red.copy(alpha = 0.85f),
            style = MaterialTheme.typography.labelSmall
        )
    }
    if (!passwordOk && passwordValue.isNotEmpty()) {
        Text(
            "Password must be at least 6 characters.",
            color = Color.Red.copy(alpha = 0.85f),
            style = MaterialTheme.typography.labelSmall
        )
    }
    if (!confirmOk && confirmValue.isNotEmpty()) {
        Text(
            "Passwords don’t match.",
            color = Color.Red.copy(alpha = 0.85f),
            style = MaterialTheme.typography.labelSmall
        )
    }

    Spacer(Modifier.height(12.dp))

    TermsRow(
        checked = termsAccepted,
        onCheckedChange = onTermsChange,
        onOpenTermsOfService = onOpenTermsOfService,
        onOpenPrivacyPolicy = onOpenPrivacyPolicy
    )

    Spacer(Modifier.height(20.dp))
    Button(
        onClick = {
            // TODO: Firebase sign-up; on success:
            onAdvance()
        },
        enabled = canSubmit,
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
                    emailValue = "test@gmail.com",
                    onEmailChange = {},
                    passwordValue = "test1234",
                    onPasswordChange = {},
                    confirmValue = "test1234",
                    onConfirmChange = {},
                    termsAccepted = true,
                    onTermsChange = {},
                    onOpenTermsOfService = {},
                    onOpenPrivacyPolicy = {},
                    onAdvance = {},
                )
            }
        }
    }
}