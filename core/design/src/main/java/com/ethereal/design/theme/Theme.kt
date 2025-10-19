package com.ethereal.design.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun BlindDateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme(
            primary = NeonRose,
            onPrimary = FogWhite,
            secondary = ChampagneGold,
            onSecondary = Color.Black,
            tertiary = SoftViolet,
            background = VelvetNavy,
            onBackground = FogWhite,
            surface = TwilightIndigo,
            onSurface = FogWhite,
            error = ErrorRose
        )
        else -> lightColorScheme(
            primary = NeonRose,
            onPrimary = Color.White,
            secondary = ChampagneGold,
            onSecondary = Color.Black,
            tertiary = SoftViolet,
            background = Color(0xFFFDFDFE),
            onBackground = Color(0xFF101010),
            surface = Color(0xFFF8F8FA),
            onSurface = Color(0xFF1C1C1E),
            error = ErrorRose

        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = BlindDateTypography,
        content = content
    )
}