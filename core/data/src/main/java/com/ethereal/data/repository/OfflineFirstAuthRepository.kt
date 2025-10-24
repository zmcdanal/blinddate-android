package com.ethereal.data.repository

import com.ethereal.common.network.BDDispatchers
import com.ethereal.common.network.Dispatcher
import com.ethereal.model.data.auth.AuthResult
import com.ethereal.network.datasource.AuthDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstAuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userDataRepository: UserDataRepository,
    @param:Dispatcher(BDDispatchers.IO) private val io: CoroutineDispatcher,
) : AuthRepository {

    override val authState = authDataSource.authState

    override fun currentUidOrNull(): String? = authDataSource.currentUidOrNull()

    override suspend fun signIn(email: String, password: String) = withContext(io) {
        val uid = authDataSource.signInWithEmail(email, password)
        userDataRepository.setAuthenticationToken(uid)
    }

    override suspend fun signUp(email: String, password: String) = withContext(io) {
        val uid = authDataSource.signUpWithEmail(email, password)
        userDataRepository.setAuthenticationToken(uid)
    }


    override suspend fun sendPasswordReset(email: String) = withContext(io) {
        authDataSource.sendPasswordReset(email)
    }

    override fun signOut() = authDataSource.signOut()

    override suspend fun signInWithGoogle(idToken: String): AuthResult = withContext(io) {
        val authResult = authDataSource.signInWithGoogle(idToken)
        userDataRepository.setAuthenticationToken(authResult.uid)
        authResult
    }
}