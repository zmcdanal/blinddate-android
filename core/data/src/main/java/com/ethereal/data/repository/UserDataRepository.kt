package com.ethereal.data.repository

import com.ethereal.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    fun setAuthenticationToken(token: String)
}