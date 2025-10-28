package com.ethereal.onboarding

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethereal.data.repository.AuthRepository
import com.ethereal.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Named

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val userDataRepository: UserDataRepository,
    @param:Named("serverClientId") private val serverClientId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<OnboardingAccountEvent>(replay = 0)
    val events: SharedFlow<OnboardingAccountEvent> = _events.asSharedFlow()

    fun updateTextField(field: TextFieldType, newValue: String) {
        _uiState.update { current ->
            when (field) {
                TextFieldType.EMAIL ->
                    current.copy(emailAddress = newValue)

                TextFieldType.PASSWORD ->
                    current.copy(password = newValue)

                TextFieldType.CONFIRM_PASSWORD ->
                    current.copy(confirmPassword = newValue)

                TextFieldType.USER ->
                    current.copy(userName = newValue)

                TextFieldType.PARTNER ->
                    current.copy(partnerName = newValue)
            }
        }
    }

    fun updateToggle(toggle: OnboardingAccountToggle, isChecked: Boolean) {
        _uiState.update { current ->
            when (toggle) {
                OnboardingAccountToggle.TERMS ->
                    current.copy(termsAccepted = isChecked)

                OnboardingAccountToggle.MARKETING ->
                    current.copy(marketingOptIn = isChecked)
            }
        }
    }

    fun updateHasPartnerToggle(toggle: Boolean) {
        _uiState.update { current ->
            current.copy(hasPartner = toggle)
        }
    }

    fun updateRadius(radius: Double) {
        viewModelScope.launch {
            userDataRepository.setDefaultRadius(radius)
        }
        _uiState.update { current ->
            current.copy(defaultRadius = radius)
        }
    }

    fun updateLocationGranted() {
        _uiState.update { it.copy(locationEnabled = true) }
    }

    fun openTermsOfService() = viewModelScope.launch {
        _events.emit(OnboardingAccountEvent.OpenTermsOfService)
    }

    fun openPrivacyPolicy() = viewModelScope.launch {
        _events.emit(OnboardingAccountEvent.OpenPrivacyPolicy)
    }


    /**
     *  Account Creation
     * */
    fun submitCreateAccount() {
        val state = _uiState.value
        if (!state.canSubmitForm) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            runCatching {
                authRepository.signUp(
                    email = state.emailAddress,
                    password = state.password
                )
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(OnboardingAccountEvent.NavigateToNextStep)
            }.onFailure { throwable ->
                val message = throwable.message ?: "Account creation failed. Please try again."
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = message)
                }
                _events.emit(OnboardingAccountEvent.ShowSnackbar(message))
            }
        }
    }

    fun submitSignInWithGoogle() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            runCatching {
                authRepository.signInWithGoogle(serverClientId)
            }.onSuccess { result ->
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(OnboardingAccountEvent.NavigateToNextStep)
            }.onFailure { t ->
                val message = t.message ?: "Google sign-up failed. Try again."
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(OnboardingAccountEvent.ShowSnackbar(message))
            }
        }
    }

}

data class OnboardingUiState(
    val userName: String = "",
    val partnerName: String? = null,
    val hasPartner: Boolean = false,
    val defaultRadius: Double = 0.0,
    val emailAddress: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val accountCreated: Boolean = false,
    val termsAccepted: Boolean = false,
    val marketingOptIn: Boolean = false,
    val isLoading: Boolean = false,
    val locationEnabled: Boolean = false,
    val errorMessage: String? = null
) {
    val isEmailValid: Boolean
        get() = Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
    val isPasswordValid: Boolean
        get() = password.length >= 6
    val isConfirmPasswordValid: Boolean
        get() = confirmPassword == password
    val canSubmitForm: Boolean
        get() = isEmailValid && isPasswordValid && isConfirmPasswordValid && termsAccepted && !isLoading
}

sealed interface OnboardingAccountEvent {
    data object NavigateToNextStep : OnboardingAccountEvent
    data class ShowSnackbar(val message: String) : OnboardingAccountEvent
    data object OpenTermsOfService : OnboardingAccountEvent
    data object OpenPrivacyPolicy : OnboardingAccountEvent
}

enum class TextFieldType {
    EMAIL,
    PASSWORD,
    CONFIRM_PASSWORD,
    USER,
    PARTNER
}

enum class OnboardingAccountToggle {
    TERMS,
    MARKETING
}