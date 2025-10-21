package com.ethereal.onboarding.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose

@Composable
fun OnboardingIntroScreen(onAdvance: () -> Unit) {
    // Intro
    Text(
        "Dinner indecision? We’ve got you.",
        style = MaterialTheme.typography.headlineSmall,
        color = FogWhite
    )
    Spacer(Modifier.height(8.dp))
    Text(
        "We pick a mystery spot you’ll love. You reveal on arrival.",
        color = FogWhite.copy(.85f)
    )
    Spacer(Modifier.height(32.dp))
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