package com.ethereal.network.datasource

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    val authState: Flow<String?>
    fun currentUidOrNull(): String?


    suspend fun signInWithEmail(email: String, password: String): String
    suspend fun signUpWithEmail(email: String, password: String): String
    suspend fun sendPasswordReset(email: String)
    fun signOut()
}