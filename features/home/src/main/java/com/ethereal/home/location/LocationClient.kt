package com.ethereal.home.location

import com.ethereal.model.data.GeoPoint
import kotlinx.coroutines.flow.Flow


interface LocationClient {
    suspend fun lastKnownOnce(): GeoPoint?
    suspend fun currentOnce(timeoutMs: Long = 2_500L): GeoPoint?

//    fun updates(intervalMs: Long = 3_000L): Flow<GeoPoint>
}