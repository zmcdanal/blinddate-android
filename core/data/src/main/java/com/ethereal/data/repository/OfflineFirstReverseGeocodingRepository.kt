package com.ethereal.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.ethereal.common.network.BDDispatchers
import com.ethereal.common.network.Dispatcher
import com.ethereal.model.data.GeoPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class OfflineFirstCityGeocodingRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:Dispatcher(BDDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CityGeocodingRepository {

    override suspend fun cityToLatLng(query: String): GeoPoint? =
        withContext(ioDispatcher) {
            runCatching {
                val geocoder = Geocoder(context, Locale.US)
                val addr: Address? =
                    if (Build.VERSION.SDK_INT >= 33) {
                        geocode33(geocoder, query)
                    } else {
                        @Suppress("DEPRECATION")
                        geocoder.getFromLocationName(query, 1)?.firstOrNull()
                    }
                addr?.let { points ->
                    GeoPoint(lat = points.latitude, lng = points.longitude)
                }
            }.getOrNull()
        }

    override suspend fun latLngToCityState(point: GeoPoint): String? =
        withContext(ioDispatcher) {
            runCatching {
                val geocoder = Geocoder(context, Locale.US)

                val address: Address? =
                    if (Build.VERSION.SDK_INT >= 33) {
                        geocodeLocation33(geocoder, point.lat, point.lng)
                    } else {
                        @Suppress("DEPRECATION")
                        geocoder.getFromLocation(point.lat, point.lng, 1)?.firstOrNull()
                    }

                address?.let { addr ->
                    // Try to get a friendly "City, ST"
                    val city = addr.locality
                        ?: addr.subAdminArea
                    val state = addr.adminArea

                    if (!city.isNullOrBlank() && !state.isNullOrBlank()) {
                        "$city, $state"
                    } else {
                        null
                    }
                }
            }.getOrNull()
        }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private suspend fun geocode33(
    geocoder: Geocoder,
    query: String
): Address? = suspendCancellableCoroutine { cont ->
    geocoder.getFromLocationName(
        query, 1,
        object : Geocoder.GeocodeListener {
            override fun onGeocode(results: MutableList<Address>) {
                cont.resume(results.firstOrNull())
            }

            override fun onError(errorMessage: String?) {
                cont.resume(null)
            }
        })
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private suspend fun geocodeLocation33(
    geocoder: Geocoder,
    latitude: Double,
    longitude: Double
): Address? = suspendCancellableCoroutine { cont ->
    geocoder.getFromLocation(
        latitude,
        longitude,
        1,
        object : Geocoder.GeocodeListener {
            override fun onGeocode(results: MutableList<Address>) {
                cont.resume(results.firstOrNull())
            }

            override fun onError(errorMessage: String?) {
                cont.resume(null)
            }
        }
    )
}
