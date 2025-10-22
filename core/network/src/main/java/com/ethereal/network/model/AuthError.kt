package com.ethereal.network.model

sealed class AuthError : Throwable() {
    data object InvalidCredentials : AuthError() {
        private fun readResolve(): Any = InvalidCredentials
    }

    data object UserNotFound : AuthError() {
        private fun readResolve(): Any = UserNotFound
    }

    data object RateLimited : AuthError() {
        private fun readResolve(): Any = RateLimited
    }

    data class Unknown(override val cause: Throwable) : AuthError()
    data object NullUid : AuthError() {
        private fun readResolve(): Any = NullUid
    }
}