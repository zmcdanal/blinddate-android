package com.ethereal.ui

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Home screen bottom bar:
 *  - Left: History
 *  - Center (emphasized): Home
 *  - Right: Profile (two-person icon)
 *
 * Show this ONLY on the Home route (Questionnaire hides it; Map uses a different bar).
 */
@Composable
fun BlindDateBottomBar(
    onHistory: () -> Unit,
    onHome: () -> Unit,
    onProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pillShape = RoundedCornerShape(24.dp)

    Box(
        modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(pillShape)
            .background(Color(0x2218181C))
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(Color(0x33FF4FD8), Color(0x334DEEFF))
                ),
                pillShape
            )
            .semantics { contentDescription = "BlindDate bottom navigation" }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // LEFT — History
            TextIconButton(
                icon = Icons.Outlined.History,
                label = "History",
                selected = false,
                onClick = onHistory
            )

            // CENTER — Home
            Box(
                Modifier
                    .size(68.dp),
                contentAlignment = Alignment.Center
            ) {
                // soft radial neon glow underlay
                Box(
                    Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0x33FF4FD8),
                                    Color(0x114DEEFF)
                                )
                            )
                        )
                )
                IconButton(
                    onClick = onHome,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1C1C22))
                        .border(
                            1.dp,
                            Brush.linearGradient(listOf(Color(0xFFFF4FD8), Color(0xFF4DEEFF))),
                            CircleShape
                        )
                        .shadow(12.dp, CircleShape, clip = false)
                        .semantics { contentDescription = "Home" }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null,
                        tint = Color(0xFFEDEDED)
                    )
                }
            }

            // RIGHT — Profile (two-person silhouette)
            TextIconButton(
                icon = Icons.Outlined.Group,
                label = "Profile",
                selected = false,
                onClick = onProfile
            )
        }
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
        targetValue = if (selected) Color(0xFFEDEDED) else Color(0xFF9EA1A6),
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

@Preview(
    name = "Bottom bar",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeBottomBar_Preview() {
    Surface(color = Color(0xFF0B0B10)) {
        Box(Modifier.padding(bottom = 16.dp)) {
            BlindDateBottomBar(
                onHome = {},
                onHistory = {},
                onProfile = {}
            )
        }
    }
}