package com.ethereal.data.repository


import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<String?>
    fun currentUidOrNull(): String?

    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun sendPasswordReset(email: String)
    fun signOut()
}