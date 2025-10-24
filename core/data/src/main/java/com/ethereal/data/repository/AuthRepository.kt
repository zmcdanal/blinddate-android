package com.ethereal.data.repository


import com.ethereal.model.data.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<String?>
    fun currentUidOrNull(): String?

    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun sendPasswordReset(email: String)
    fun signOut()

    suspend fun signInWithGoogle(idToken: String): AuthResult
}