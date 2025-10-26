package com.ethereal.common.navigation.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ethereal.common.navigation.BottomTabs

@Composable
fun BottomTabsBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    container: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
    indicator: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
    shadow: Dp = 12.dp,
) {
    Surface(
        color = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(shadow, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), clip = false)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(
                    brush = Brush.verticalGradient(
                        0f to container,
                        1f to container.copy(alpha = 0.98f)
                    )
                )
        ) {
            NavigationBar row@{
                BottomTabs.forEach { dest ->
                    val selected = currentRoute?.startsWith(dest.route) == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = { onNavigate(dest.route) },

                        icon = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .size(
                                        width = animateDpAsState(
                                            if (selected) 56.dp else 40.dp,
                                            animationSpec = spring(
                                                dampingRatio = 0.8f,
                                                stiffness = 400f
                                            )
                                        ).value,
                                        height = 40.dp
                                    )
                            ) {
                                this@row.AnimatedVisibility(
                                    visible = selected,
                                    enter = fadeIn(tween(150)),
                                    exit = fadeOut(tween(120))
                                ) {
                                    Box(
                                        Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                            )
                                    )
                                }
                                Icon(dest.icon, contentDescription = dest.label)
                            }
                        },

                        label = {
                            this@row.AnimatedVisibility(
                                visible = selected,
                                enter = fadeIn(tween(150)),
                                exit = fadeOut(tween(120))
                            ) {
                                Text(dest.label)
                            }
                        },

                        alwaysShowLabel = false,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }

            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
            )
        }
    }
}

@Composable
fun ActionBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
    fabDiameter: Dp = 56.dp,
    fabSidePadding: Dp = 16.dp
) {
    val centerGap = fabDiameter + fabSidePadding * 2

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), clip = false)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(containerColor)
    ) {
        BottomAppBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            actions = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left item (History)
                    IconButton(onClick = { onNavigate("history") }) {
                        val selected = currentRoute?.startsWith("history") == true
                        Icon(
                            Icons.Outlined.History, contentDescription = "History",
                            tint = if (selected) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(Modifier.width(centerGap))

                    // Right item (Profile)
                    IconButton(onClick = { onNavigate("profile") }) {
                        val selected = currentRoute?.startsWith("profile") == true
                        Icon(
                            Icons.Outlined.Person, contentDescription = "Profile",
                            tint = if (selected) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        )

        // Top divider (optional)
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        )
    }
}
