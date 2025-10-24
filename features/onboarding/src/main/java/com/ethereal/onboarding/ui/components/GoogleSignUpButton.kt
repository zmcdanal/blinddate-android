package com.ethereal.onboarding.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ethereal.design.R
import com.ethereal.design.theme.BlindDateTypography
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose

@Composable
fun GoogleSignUpButton(
    loading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = !loading,
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, NeonRose.copy(alpha = 0.35f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = NeonRose,
            contentColor = FogWhite,
            disabledContainerColor = NeonRose.copy(alpha = 0.6f),
            disabledContentColor = FogWhite.copy(alpha = 0.6f)
        ),
        modifier = modifier
            .fillMaxWidth(0.85f)
            .height(56.dp)
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .height(24.dp)
                .padding(end = 12.dp)
                .aspectRatio(1f)
                .background(Color.White, shape = androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_g),
                contentDescription = "Google",
                tint = Color.Unspecified,
                modifier = Modifier
                    .height(18.dp)
                    .aspectRatio(1f)
            )
        }

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.height(18.dp),
                strokeWidth = 2.dp,
                color = FogWhite
            )
        } else {
            Text(
                text = "Continue with Google",
                style = BlindDateTypography.titleMedium,
                color = FogWhite
            )
        }
    }
}

