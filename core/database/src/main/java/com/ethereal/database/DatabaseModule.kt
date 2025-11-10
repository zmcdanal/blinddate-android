package com.ethereal.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.ethereal.database.BlindDateDatabase
import com.ethereal.database.dao.DateDetailsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): BlindDateDatabase =
        Room.databaseBuilder(
            context,
            BlindDateDatabase::class.java,
            "blinddate.db"
        )
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideDateDetailsDao(db: BlindDateDatabase): DateDetailsDao = db.dateDetailsDao()
}