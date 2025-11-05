package com.ethereal.data.repository

import com.ethereal.common.network.BDDispatchers
import com.ethereal.common.network.Dispatcher
import com.ethereal.database.dao.DateDetailsDao
import com.ethereal.database.model.asExternalModel
import com.ethereal.database.model.toEntity
import com.ethereal.model.data.DateDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFistDateDetailsRepository @Inject constructor(
    private val dao: DateDetailsDao,
    @param:Dispatcher(BDDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : DateDetailsRepository {

    override suspend fun save(details: DateDetails) = withContext(ioDispatcher) {
        dao.insert(details.toEntity())
    }

    override suspend fun getOrNull(): DateDetails? = withContext(ioDispatcher) {
        dao.get()?.asExternalModel()
    }

    override fun observe(): Flow<DateDetails?> =
        dao.observe().map { it?.asExternalModel() }

    override suspend fun clear() = withContext(ioDispatcher) { dao.clear() }
}