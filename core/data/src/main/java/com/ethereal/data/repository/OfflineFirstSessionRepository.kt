package com.ethereal.data.repository

import com.ethereal.common.network.BDDispatchers
import com.ethereal.common.network.Dispatcher
import com.ethereal.model.data.auth.AuthState
import com.ethereal.network.datasource.AuthDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstSessionRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
    @param:Dispatcher(BDDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : SessionRepository {

    override val authState: Flow<AuthState> =
        authDataSource.authState
            .map { uid -> if (uid != null) AuthState.Authenticated(uid) else AuthState.Unauthenticated }
            .onStart { emit(AuthState.Loading) }
            .distinctUntilChanged()
            .flowOn(ioDispatcher) // <- ensure mapping is not on Main


    override fun currentUidOrNull(): String? = authDataSource.currentUidOrNull()
    override suspend fun signOut() {
        authDataSource.signOut()
    }
}