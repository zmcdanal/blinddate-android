package com.ethereal.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethereal.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

    private val submitMutex = Mutex()

    fun onEmailChange(email: String) =
        _uiState.update { it.copy(email = email, error = null) }

    fun onPasswordChange(password: String) =
        _uiState.update { it.copy(password = password, error = null) }

    fun clearError() = _uiState.update { it.copy(error = null) }

    fun loginEmailPassword(onLoginRequest: (Boolean) -> Unit) = viewModelScope.launch {
        submitMutex.withLock {
            val email = _uiState.value.email.trim().lowercase()
            val pass = _uiState.value.password

            if (email.isEmpty() || pass.isEmpty()) {
                _uiState.update { it.copy(error = "Email and password required") }
                return@withLock
            }

            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val userUid = authRepository.signIn(email, pass)
                if (userUid.isNotEmpty()) {
                    _uiState.update { it.copy(isLoading = false) }
                    onLoginRequest(true)
                }
                // isLoggedIn will flip via authState; we clear password there.
            } catch (exception: Exception) {
                Log.d("LoginViewModel", "loginEmailPassword: $exception")
                onLoginRequest(false)
                if (exception is CancellationException) throw exception
                _uiState.update { it.copy(isLoading = false, error = mapAuthError(exception)) }
            }
        }
    }

    fun sendPasswordReset(email: String) = viewModelScope.launch {
        //TODO: send password reset email
    }

    private fun mapAuthError(t: Throwable): String {
        val name = t::class.simpleName ?: "Auth error"
        return when (name) {
            "InvalidCredentials" -> "Invalid email or password."
            "UserNotFound" -> "No account found for that email."
            "RateLimited" -> "Too many attempts. Try again later."
            "NullUid" -> "Something went wrong. Please try again."
            else -> t.message ?: "Login failed."
        }
    }
}

sealed interface AuthState {
    data object Loading : AuthState
    data class Authenticated(val uid: String) : AuthState
    data object Unauthenticated : AuthState
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

sealed interface LoginEvent {
    data class ShowToast(val message: String) : LoginEvent
}
