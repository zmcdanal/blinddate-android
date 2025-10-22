package com.ethereal.data.repository

import com.ethereal.common.network.BDDispatchers
import com.ethereal.common.network.Dispatcher
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class OfflineFirstUserDataRepository @Inject constructor(
    @param:Dispatcher(BDDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : UserDataRepository {

}