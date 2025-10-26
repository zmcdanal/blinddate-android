package com.ethereal.data.repository.di

import com.ethereal.data.repository.AuthRepository
import com.ethereal.data.repository.OfflineFirstAuthRepository
import com.ethereal.data.repository.OfflineFirstSessionRepository
import com.ethereal.data.repository.OfflineFirstUserDataRepository
import com.ethereal.data.repository.SessionRepository
import com.ethereal.data.repository.UserDataRepository
import dagger.hilt.components.SingletonComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindings {

    @Binds
    @Singleton
    abstract fun bindUserDataRepository(impl: OfflineFirstUserDataRepository): UserDataRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: OfflineFirstAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: OfflineFirstSessionRepository): SessionRepository
}