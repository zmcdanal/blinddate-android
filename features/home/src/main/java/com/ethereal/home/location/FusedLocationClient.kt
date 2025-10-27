package com.ethereal.home.location


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.ethereal.model.data.GeoPoint
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FusedLocationClient @Inject constructor(
    @param:ApplicationContext private val context: Context
) : LocationClient {

    companion object {
        private const val TAG = "FusedLocationClient"
    }

    @SuppressLint("MissingPermission")
    override fun lastKnownOnce(): Flow<GeoPoint?> = flow {
        val fused = LocationServices.getFusedLocationProviderClient(context)
        val loc = runCatching { fused.lastLocation.await() }.getOrNull()
        emit(loc?.let { GeoPoint(it.latitude, it.longitude) })
    }

    @SuppressLint("MissingPermission")
    override fun currentOnce(timeoutMs: Long): Flow<GeoPoint?> = flow {
        val fused = LocationServices.getFusedLocationProviderClient(context)
        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val priority = if (hasFine)
            Priority.PRIORITY_HIGH_ACCURACY
        else
            Priority.PRIORITY_BALANCED_POWER_ACCURACY

        val cts = CancellationTokenSource()
        try {
            val loc = withTimeoutOrNull(timeoutMs) {
                fused.getCurrentLocation(priority, cts.token).await()
            }
            emit(loc?.let { GeoPoint(it.latitude, it.longitude) })
        } catch (exception: Exception) {
            Log.d(TAG, "currentOnce: error = $exception")
            emit(null)
        } finally {
            cts.cancel()
        }
    }

//    @SuppressLint("MissingPermission")
//    override fun updates(intervalMs: Long): Flow<GeoPoint> = callbackFlow {
//        val fused = LocationServices.getFusedLocationProviderClient(context)
//        val req = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalMs)
//            .setMinUpdateIntervalMillis(intervalMs)
//            .build()
//        val cb = object : LocationCallback() {
//            override fun onLocationResult(res: LocationResult) {
//                res.lastLocation?.let { trySend(GeoPoint(it.latitude, it.longitude)) }
//            }
//        }
//        fused.requestLocationUpdates(req, cb, context.mainLooper)
//        awaitClose { fused.removeLocationUpdates(cb) }
//    }
}