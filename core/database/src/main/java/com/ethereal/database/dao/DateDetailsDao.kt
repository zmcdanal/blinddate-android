package com.ethereal.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ethereal.database.model.DateDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDetailsDao {
    // write/replace the single row
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DateDetailsEntity): Long

    // read/observe the single row
    @Query("SELECT * FROM date_details WHERE id = 0")
    suspend fun get(): DateDetailsEntity?

    @Query("SELECT * FROM date_details WHERE id = 0")
    fun observe(): Flow<DateDetailsEntity?>

    // clear it when the trip is done
    @Query("DELETE FROM date_details WHERE id = 0")
    suspend fun clear()

    // (optional safety nuke if you ever wrote multiple in the past)
    @Query("DELETE FROM date_details")
    suspend fun clearAll()
}