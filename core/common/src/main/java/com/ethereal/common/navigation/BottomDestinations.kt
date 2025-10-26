package com.ethereal.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object History : BottomDestination("history", "History", Icons.Outlined.History)
    data object Profile : BottomDestination("profile", "Profile", Icons.Outlined.Person)
}

val BottomTabs = listOf(BottomDestination.History, BottomDestination.Profile)