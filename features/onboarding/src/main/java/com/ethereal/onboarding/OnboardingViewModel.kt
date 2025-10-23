package com.ethereal.onboarding

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethereal.data.repository.AuthRepository
import com.ethereal.data.repository.utils.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    syncManager: SyncManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    val isSyncing = syncManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = false,
        )

    private var _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private var _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private var _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()


    fun updateTextField(newValue: String, type: LoginFieldType) {
        try {
            when (type) {
                LoginFieldType.EMAIL -> _email.update { newValue }
                LoginFieldType.PASSWORD -> _password.update { newValue }
                LoginFieldType.CONFIRM_PASSWORD -> _confirmPassword.update { newValue }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    /**
     * These are only temporary while I update UI
     * */
    val isPartner = mutableStateOf(false)
    val displayName = mutableStateOf("")
    val partnerName = mutableStateOf("")
    val termsAccepted = mutableStateOf(false)
    val defaultRadius = mutableIntStateOf(10) // miles
    val locationEnabled = mutableStateOf(false)
    val accountCreated = mutableStateOf(false)

    enum class LoginFieldType {
        EMAIL,
        PASSWORD,
        CONFIRM_PASSWORD
    }
}