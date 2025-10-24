package com.ethereal.blinddate.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import com.ethereal.blinddate.R

@Module
@InstallIn(SingletonComponent::class)
object OAuthModule {

    @Provides
    @Named("serverClientId")
    fun provideServerClientId(
        @ApplicationContext context: Context
    ): String = context.getString(R.string.server_client_id)
}