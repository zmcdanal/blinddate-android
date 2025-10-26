package com.ethereal.blinddate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethereal.data.repository.SessionRepository
import com.ethereal.model.data.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    sessionRepository: SessionRepository
) : ViewModel() {
    val authState: StateFlow<AuthState> =
        sessionRepository.authState
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AuthState.Loading)
}