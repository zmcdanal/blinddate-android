package com.ethereal.design.reusables

import android.graphics.Canvas
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.NeonRose

@Composable
fun NeonSpinner() {
    val rotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(900, easing = LinearEasing))
    )
    Canvas(Modifier
        .size(40.dp)
        .graphicsLayer { rotationZ = rotation }) {
        val stroke = Stroke(width = 8f, cap = StrokeCap.Round)
        drawArc(
            color = NeonRose,
            startAngle = 0f,
            sweepAngle = 270f,
            useCenter = false,
            style = stroke
        )
    }
}
