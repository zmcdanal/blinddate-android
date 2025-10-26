package com.ethereal.data.repository

import com.ethereal.model.data.auth.AuthState
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val authState: Flow<AuthState>
    fun currentUidOrNull(): String?
    suspend fun signOut()
}