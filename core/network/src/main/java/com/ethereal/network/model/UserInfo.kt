package com.ethereal.network.model

import com.google.firebase.Timestamp

data class UserRemote(
    val displayName: String? = null,
    val joinedAt: Timestamp? = null,
    //val preferences: UserPreferences? = null
)