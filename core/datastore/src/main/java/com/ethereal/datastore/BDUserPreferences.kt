package com.ethereal.datastore

import androidx.datastore.core.DataStore
import com.ethereal.model.data.UserData
import kotlinx.coroutines.flow.map
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
                userId = it.userId,
            )
        }

}