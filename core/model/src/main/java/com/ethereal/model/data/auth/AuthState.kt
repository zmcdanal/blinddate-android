package com.ethereal.model.data.auth

sealed interface AuthState {
    data object Loading : AuthState
    data class Authenticated(val uid: String) : AuthState
    data object Unauthenticated : AuthState
}