package com.ethereal.data.repository

import com.ethereal.common.network.BDDispatchers
import com.ethereal.common.network.Dispatcher
import com.ethereal.datastore.BDUserPreferences
import com.ethereal.model.data.UserData
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OfflineFirstUserDataRepository @Inject constructor(
    private val bdUserPreferences: BDUserPreferences,
    @param:Dispatcher(BDDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : UserDataRepository {

    override val userData: Flow<UserData>
        get() = bdUserPreferences.userData

    override fun setAuthenticationToken(token: String) {
        CoroutineScope(ioDispatcher).launch {
            bdUserPreferences.setAuthenticationToken(token)
        }
    }

}