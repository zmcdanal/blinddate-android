package com.ethereal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ethereal.database.dao.DateDetailsDao
import com.ethereal.database.model.DateDetailsEntity

@Database(
    entities = [DateDetailsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BlindDateDatabase : RoomDatabase() {
    abstract fun dateDetailsDao(): DateDetailsDao
}