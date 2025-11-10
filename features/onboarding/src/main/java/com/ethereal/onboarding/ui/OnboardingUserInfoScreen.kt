package com.ethereal.onboarding.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.common.Constants
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.BlindDateTypography
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
import com.ethereal.ui.BlindDateBackground
import com.ethereal.design.reusables.NameField
import com.ethereal.design.reusables.SegmentedSoloPartnerToggle

@Composable
fun OnboardingUserInfoScreen(
    isPartner: Boolean,
    onPartnerToggleChange: (Boolean) -> Unit,
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    partnerName: String,
    onPartnerNameChange: (String) -> Unit,
    defaultRadius: Int,
    onDefaultRadiusChange: (Int) -> Unit,
    onAdvance: () -> Unit
) {
    Text(
        "How will you be using BlindDate?",
        color = FogWhite.copy(alpha = .85f),
        style = BlindDateTypography.titleLarge
    )

    Spacer(Modifier.height(16.dp))

    // Mode: Solo | With Partner
    SegmentedSoloPartnerToggle(
        isPartner = isPartner,
        onToggle = onPartnerToggleChange
    )

    Spacer(Modifier.height(16.dp))

    // Names
    NameField(
        placeholder = "Your Name",
        value = displayName,
        onValueChange = onDisplayNameChange,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    if (isPartner) {
        NameField(
            placeholder = "Your Partner's Name",
            value = partnerName,
            onValueChange = onPartnerNameChange,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }

    Spacer(Modifier.height(8.dp))

    Text(
        "Default distance radius (mi) — change anytime in Profile",
        color = FogWhite.copy(alpha = .85f)
    )

    Spacer(Modifier.height(8.dp))

    var temp by remember(defaultRadius) { mutableFloatStateOf(defaultRadius.toFloat()) }
    Slider(
        value = temp,
        onValueChange = { onDefaultRadiusChange(it.toInt()) },
        onValueChangeFinished = { onDefaultRadiusChange(temp.toInt().coerceIn(Constants.MIN_RADIUS_MILES, Constants.MAX_RADIUS_MILES)) },
        valueRange = Constants.MIN_RADIUS_MILES.toFloat()..Constants.MAX_RADIUS_MILES.toFloat(),
        modifier = Modifier.fillMaxWidth(0.85f)
    )
    Text("${temp.toInt()} miles", color = FogWhite)



    Spacer(Modifier.height(20.dp))
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
    ) { Text("Continue") }
}

@Preview
@Composable
fun PreviewOnboardingUserInfoScreen() {
    BlindDateTheme {
        BlindDateBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnboardingUserInfoScreen(
                    isPartner = true,
                    onPartnerToggleChange = {},
                    displayName = "Daniel",
                    onDisplayNameChange = {},
                    partnerName = "Jessica",
                    onPartnerNameChange = {},
                    defaultRadius = 15,
                    onDefaultRadiusChange = {},
                ) { }
            }
        }
    }
}