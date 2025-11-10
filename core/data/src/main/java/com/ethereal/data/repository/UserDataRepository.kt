package com.ethereal.data.repository

import com.ethereal.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setAuthenticationToken(token: String)

    suspend fun setDefaultRadius(radius: Int)
}