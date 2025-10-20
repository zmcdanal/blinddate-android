package com.ethereal.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.*

@Composable
fun BlindDateBottomBar(
    onHistory: () -> Unit,
    onHome: () -> Unit,
    onProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    // --- Layout spec ---
    val pillHeight = 64.dp
    val pillCorner = 28.dp
    val pillStroke = 2.dp
    val puckSize = 60.dp
    val puckOffsetY = (-26).dp
    val notchExtraDip = 6.dp
    val sidePadding = 16.dp
    val rowPadding = 18.dp

    val notchRadius = puckSize / 2
    val notchGap = 2.dp + pillStroke / 2
    val notchHalfWidth = notchRadius + notchGap + 30.dp

    Box(
        modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .height(96.dp)
            .padding(horizontal = sidePadding, vertical = 8.dp)
            .semantics { contentDescription = "BlindDate bottom navigation" }
    ) {
        // Background surface
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .height(pillHeight)
                .fillMaxWidth()
                .clip(RoundedCornerShape(pillCorner))
                .background(colorScheme.surface.copy(alpha = 0.15f))
        )

        // Border with swoop
        NotchedPillBorder(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(pillHeight)
                .fillMaxWidth(),
            cornerRadius = pillCorner,
            strokeWidth = pillStroke,
            notchRadius = notchRadius,
            notchGap = notchGap,
            notchHalfWidth = notchHalfWidth,
            extraDip = notchExtraDip,
            borderBrush = Brush.horizontalGradient(
                listOf(NeonRose.copy(alpha = 0.3f), SoftViolet.copy(alpha = 0.3f))
            )
        )

        // Buttons row
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .height(pillHeight)
                .fillMaxWidth()
                .padding(horizontal = rowPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextIconButton(
                icon = Icons.Outlined.History,
                label = "History",
                selected = false,
                onClick = onHistory
            )

            Spacer(Modifier.width(notchHalfWidth * 2 + 24.dp))

            TextIconButton(
                icon = Icons.Outlined.Group,
                label = "Profile",
                selected = false,
                onClick = onProfile
            )
        }

        // Glow
        Box(
            Modifier
                .size(puckSize + 24.dp)
                .align(Alignment.Center)
                .offset(y = puckOffsetY + 8.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(NeonRose.copy(alpha = 0.25f), Color.Transparent)
                    )
                )
        )

        // Floating Home puck
        IconButton(
            onClick = onHome,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = puckOffsetY)
                .size(puckSize)
                .clip(CircleShape)
                .background(colorScheme.surface)
                .border(
                    1.5.dp,
                    Brush.linearGradient(listOf(NeonRose, SoftViolet)),
                    CircleShape
                )
                .shadow(14.dp, CircleShape, clip = false)
                .semantics { contentDescription = "Home" }
        ) {
            Icon(
                Icons.Filled.Home,
                contentDescription = null,
                tint = FogWhite
            )
        }
    }
}

@Composable
private fun NotchedPillBorder(
    modifier: Modifier,
    cornerRadius: Dp,
    strokeWidth: Dp,
    notchRadius: Dp,
    notchGap: Dp,
    notchHalfWidth: Dp,
    extraDip: Dp,
    borderBrush: Brush
) {
    val d = LocalDensity.current
    val cr = with(d) { cornerRadius.toPx() }
    val sw = with(d) { strokeWidth.toPx() }
    val R = with(d) { notchRadius.toPx() }
    val G = with(d) { notchGap.toPx() }
    val W = with(d) { notchHalfWidth.toPx() }
    val extra = with(d) { extraDip.toPx() }

    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f

        val leftX = cx - W
        val rightX = cx + W
        val dipY = R + G + extra

        val path = Path().apply {
            moveTo(cr, 0f)
            lineTo(leftX, 0f)

            cubicTo(
                leftX + W * 0.35f, 0f,
                cx - W * 0.55f, dipY * 0.95f,
                cx, dipY
            )
            cubicTo(
                cx + W * 0.55f, dipY * 0.95f,
                rightX - W * 0.35f, 0f,
                rightX, 0f
            )

            lineTo(w - cr, 0f)
            quadraticTo(w, 0f, w, cr)
            lineTo(w, h - cr)
            quadraticTo(w, h, w - cr, h)
            lineTo(cr, h)
            quadraticTo(0f, h, 0f, h - cr)
            lineTo(0f, cr)
            quadraticTo(0f, 0f, cr, 0f)
            close()
        }

        drawPath(path, brush = borderBrush, style = Stroke(width = sw))
    }
}

@Composable
private fun TextIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint by animateColorAsState(
        targetValue = if (selected) FogWhite else FogWhite.copy(alpha = 0.6f),
        label = "Bottom item tint"
    )
    Column(
        modifier = Modifier
            .sizeIn(minWidth = 72.dp, minHeight = 48.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .semantics { contentDescription = label },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        Text(label, color = tint, style = MaterialTheme.typography.labelSmall)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun PreviewBottomBar() {
    BlindDateTheme {
        BlindDateBottomBar(onHistory = {}, onHome = {}, onProfile = {})
    }
}
