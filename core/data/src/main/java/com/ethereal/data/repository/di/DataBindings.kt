package com.ethereal.data.repository.di

import com.ethereal.data.repository.AuthRepository
import com.ethereal.data.repository.OfflineFirstAuthRepository
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
    abstract fun bindAuthRepository(impl: OfflineFirstAuthRepository): AuthRepository
}