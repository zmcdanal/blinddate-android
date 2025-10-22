package com.ethereal.network.di


import com.ethereal.network.datasource.AuthDataSource
import com.ethereal.network.datasource.FirebaseAuthDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindings {
    @Binds
    @Singleton
    abstract fun bindFirebaseDataSource(impl: FirebaseAuthDataSource): AuthDataSource
}