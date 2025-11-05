package com.ethereal.data.repository

import com.ethereal.model.data.DateDetails
import kotlinx.coroutines.flow.Flow

interface DateDetailsRepository {
    suspend fun save(details: DateDetails): Long
    suspend fun getOrNull(): DateDetails?
    fun observe(): Flow<DateDetails?>
    suspend fun clear()
}