package com.ethereal.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.R as designR
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.BlindDateTypography
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
import com.ethereal.onboarding.R
import com.ethereal.ui.BlindDateBackground

@Composable
fun OnboardingIntroScreen(onAdvance: () -> Unit) {
    // Title - Text
    Text(
        text = stringResource(R.string.onboarding_intro_title),
        style = BlindDateTypography.headlineLarge,
        textAlign = TextAlign.Center,
        color = FogWhite,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .padding(top = 16.dp)
    )

    // Mystery Restaurant - Image
    Image(
        painter = painterResource(designR.drawable.mystery_restuarant),
        contentDescription = "Mystery Restaurant",
        modifier = Modifier
            .size(300.dp)
            .padding(top = 16.dp, bottom = 16.dp)
    )

    // Description - Text
    Text(
        stringResource(R.string.onboarding_intro_description),
        style = BlindDateTypography.bodyMedium,
        textAlign = TextAlign.Center,
        color = FogWhite.copy(.85f),
        modifier = Modifier
            .fillMaxWidth(.9f)
    )

    Spacer(Modifier.height(32.dp))

    // Get Started - Button
    Button(
        onClick = onAdvance,
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonRose,
            contentColor = FogWhite
        ),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(56.dp)
    ) { Text("Get started") }
}

@Preview
@Composable
fun PreviewOnboardingIntroScreen() {
    BlindDateTheme {
        BlindDateBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnboardingIntroScreen { }
            }
        }
    }
}