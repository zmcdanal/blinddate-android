package com.ethereal.onboarding

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    /**
     * These are only temporary while I update UI
     * */
    val isPartner = mutableStateOf(false)
    val displayName = mutableStateOf("")
    val partnerName = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirm = mutableStateOf("")
    val termsAccepted = mutableStateOf(false)
    val defaultRadius = mutableIntStateOf(10) // miles
    val locationEnabled = mutableStateOf(false)
    val accountCreated = mutableStateOf(false)
}