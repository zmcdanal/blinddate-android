package com.ethereal.home.location


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.ethereal.model.data.GeoPoint
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FusedLocationClient @Inject constructor(
    @param:ApplicationContext private val context: Context
) : LocationClient {

    @SuppressLint("MissingPermission")
    override suspend fun lastKnownOnce(): GeoPoint? {
        val fused = LocationServices.getFusedLocationProviderClient(context)
        val loc = runCatching { fused.lastLocation.await() }.getOrNull()
        return loc?.let { GeoPoint(it.latitude, it.longitude) }
    }

    @SuppressLint("MissingPermission")
    override suspend fun currentOnce(timeoutMs: Long): GeoPoint? {
        val fused = LocationServices.getFusedLocationProviderClient(context)

        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val priority = if (hasFine)
            Priority.PRIORITY_HIGH_ACCURACY
        else
            Priority.PRIORITY_BALANCED_POWER_ACCURACY

        val cts = CancellationTokenSource()
        return try {
            val loc = withTimeoutOrNull(timeoutMs) {
                fused.getCurrentLocation(priority, cts.token).await()
            }
            loc?.let { GeoPoint(it.latitude, it.longitude) }
        } finally {
            cts.cancel()
        }
    }
}