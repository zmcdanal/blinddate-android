package com.ethereal.blinddate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ethereal.blinddate.AuthViewModel
import com.ethereal.model.data.auth.AuthState

object Entry {
    const val ROUTE = "entry"
}

fun NavGraphBuilder.entryGraph(
    onToHome: () -> Unit,
    onToLogin: () -> Unit,
) {
    composable(Entry.ROUTE) {
        EntryRoute(onToHome = onToHome, onToLogin = onToLogin)
    }
}

@Composable
private fun EntryRoute(
    onToHome: () -> Unit,
    onToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.authState.collectAsStateWithLifecycle()

    when (state) { //TODO: Add Loading Screen
        is AuthState.Loading -> Unit
        is AuthState.Authenticated -> LaunchedEffect(Unit) { onToHome() }
        is AuthState.Unauthenticated -> LaunchedEffect(Unit) { onToLogin() }
    }
}