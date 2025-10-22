package com.ethereal.network.datasource

import android.util.Log
import com.ethereal.network.model.AuthError
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth
) : AuthDataSource {

    companion object {
        private const val TAG: String = "FirebaseAuthDataSource"
    }

    override val authState: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser?.uid) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun currentUidOrNull(): String? = auth.currentUser?.uid

    override suspend fun signInWithEmail(email: String, password: String): String = try {
        val response = auth.signInWithEmailAndPassword(email, password).await()
        val uid = response.user?.uid
        if (uid == null) {
            auth.signOut()
            throw AuthError.NullUid
        }
        uid
    } catch (exception: FirebaseAuthInvalidCredentialsException) {
        Log.d(TAG, "Invalid credentials: ", exception)
        throw AuthError.InvalidCredentials
    } catch (exception: FirebaseAuthInvalidUserException) {
        Log.d(TAG, "Invalid credentials: ", exception)
        throw AuthError.UserNotFound
    } catch (exception: FirebaseTooManyRequestsException) {
        Log.d(TAG, "Too many requests: ", exception)
        throw AuthError.RateLimited
    } catch (throwable: Throwable) {
        Log.d(TAG, "Unknown error: ", throwable)
        throw AuthError.Unknown(throwable)
    }

    override suspend fun signUpWithEmail(email: String, password: String): String = try {
        val response = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = response.user?.uid
        if (uid == null) {
            auth.signOut()
            throw AuthError.NullUid
        }
        uid
    } catch (throwable: Throwable) {
        Log.d(TAG, "Unknown error:", throwable)
        throw AuthError.Unknown(throwable)
    }

    override suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override fun signOut() = auth.signOut()
}