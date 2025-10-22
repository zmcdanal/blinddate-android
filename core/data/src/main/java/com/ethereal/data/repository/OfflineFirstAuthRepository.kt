package com.ethereal.data.repository

import com.ethereal.common.network.BDDispatchers
import com.ethereal.common.network.Dispatcher
import com.ethereal.network.datasource.AuthDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstAuthRepository @Inject constructor(
    private val remote: AuthDataSource,
    private val userDataRepository: UserDataRepository,
    @param:Dispatcher(BDDispatchers.IO) private val io: CoroutineDispatcher,
) : AuthRepository {

    override val authState = remote.authState

    override fun currentUidOrNull(): String? = remote.currentUidOrNull()

    override suspend fun signIn(email: String, password: String) = withContext(io) {
        val uid = remote.signInWithEmail(email, password)
        userDataRepository.setAuthenticationToken(uid)
    }

    override suspend fun signUp(email: String, password: String) = withContext(io) {
        val uid = remote.signUpWithEmail(email, password)
        userDataRepository.setAuthenticationToken(uid)
    }


    override suspend fun sendPasswordReset(email: String) = withContext(io) {
        remote.sendPasswordReset(email)
    }

    override fun signOut() = remote.signOut()
}