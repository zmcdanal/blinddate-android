package com.ethereal.onboarding.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
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
    val emailOk = android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()
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
    Row(
        modifier = Modifier.fillMaxWidth(0.85f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = termsAccepted, onCheckedChange = onTermsChange)
        Spacer(Modifier.width(8.dp))
        Text(
            "I agree to the Terms of Service and Privacy Policy",
            color = FogWhite,
            style = MaterialTheme.typography.bodyMedium
        )
    }

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