package com.ethereal.data.sync.di

import com.ethereal.data.sync.DefaultSyncManager
import com.ethereal.data.sync.SyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncBindings {
    @Binds
    @Singleton
    abstract fun bindSyncManager(impl: DefaultSyncManager): SyncManager
}