package com.ethereal.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.room.util.copy
import com.ethereal.datastore.proto.UserPreferences
import com.ethereal.datastore.proto.copy
import com.ethereal.model.data.UserData
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class BDUserPreferences @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {

    companion object {
        private const val TAG: String = "BDUserPreferences"
    }

    val userData = userPreferences.data
        .map {
            UserData(
                authenticationToken = it.authenticationToken,
            )
        }

    suspend fun setAuthenticationToken(token: String) {
        try {
            userPreferences.updateData {
                it.copy {
                    authenticationToken = token
                }
            }
        } catch (ioException: IOException) {
            Log.e(
                TAG,
                "Failed to update user preferences",
                ioException
            )
        }
    }

}