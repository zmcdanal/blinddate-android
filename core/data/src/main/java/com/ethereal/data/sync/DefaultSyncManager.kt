package com.ethereal.data.sync

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSyncManager @Inject constructor() : SyncManager {
    private val _isSyncing = MutableStateFlow(false)
    override val isSyncing: StateFlow<Boolean> = _isSyncing
    fun begin() {
        _isSyncing.value = true
    }

    fun end() {
        _isSyncing.value = false
    }
}