package com.ethereal.home.location

import com.ethereal.model.data.GeoPoint
import kotlinx.coroutines.flow.Flow


interface LocationClient {

    fun lastKnownOnce(): Flow<GeoPoint?>

    /** Emits one fresh GPS update */
    fun currentOnce(timeoutMs: Long = 2_500L): Flow<GeoPoint?>

//    fun updates(intervalMs: Long = 3_000L): Flow<GeoPoint>
}