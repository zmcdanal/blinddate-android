package com.ethereal.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.ethereal.datastore.proto.UserPreferences
import com.ethereal.model.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BDUserPreferences @Inject constructor(
    private val dataStore: DataStore<UserPreferences>,
) {
    companion object {
        private const val TAG = "BDUserPreferences"
    }

    // Simple primitive flow
    val authToken: Flow<String> = dataStore.data.map { it.authenticationToken }

    // Mapped domain object
    val userData: Flow<UserData> = dataStore.data.map { prefs ->
        UserData(
            authenticationToken = prefs.authenticationToken,
            defaultRadius = prefs.defaultRadius,
        )
    }

    suspend fun setAuthenticationToken(token: String) {
        try {
            dataStore.updateData { current ->
                current.toBuilder()
                    .setAuthenticationToken(token)
                    .build()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to update user preferences", e)
        }
    }

    suspend fun setDefaultRadius(radius: Int) {
        try {
            dataStore.updateData { current ->
                current.toBuilder()
                    .setDefaultRadius(radius)
                    .build()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to update user preferences", e)
        }
    }

    suspend fun clearUserData() {
        try {
            dataStore.updateData { current ->
                current.toBuilder()
                    .clearAuthenticationToken()
                    .build()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to clear user preferences", e)
        }
    }
}
