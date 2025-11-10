package com.ethereal.home.components.map

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ethereal.design.theme.NeonRose
import com.ethereal.model.data.DateDetails
import com.ethereal.model.utils.milesToMeters
import com.ethereal.model.utils.toLatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.*

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun HomeMap(
    dateDetails: DateDetails,
    modifier: Modifier = Modifier,
    interactive: Boolean = true
) {
    val isLoading = dateDetails.mapData.mapLoading
    val mapData = dateDetails.mapData
    val center = mapData.userLocation?.toLatLng()
    val radiusMeters = milesToMeters(mapData.radiusMiles)

    val markerState = remember { MarkerState() }
    val cameraState = rememberCameraPositionState()

    LaunchedEffect(center?.latitude, center?.longitude) {
        center?.let { markerState.position = it }
    }

    Box(modifier) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraState,
            properties = MapProperties(isMyLocationEnabled = false, mapType = MapType.NORMAL),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                scrollGesturesEnabled = interactive && !isLoading,
                zoomGesturesEnabled = interactive && !isLoading,
                rotationGesturesEnabled = interactive && !isLoading,
                tiltGesturesEnabled = interactive && !isLoading
            )
        ) {
            if (center != null) {
                key(center.latitude, center.longitude, radiusMeters) {
                    Circle(
                        center = markerState.position,
                        radius = maxOf(0.0, radiusMeters),
                        strokeWidth = 5f,
                        strokeColor = NeonRose,
                        fillColor = NeonRose.copy(alpha = 0.2f)
                    )
                }
                Marker(state = markerState, title = "You")

                MapEffect(center.latitude, center.longitude, radiusMeters) { map ->
                    val update = if (radiusMeters > 1.0) {
                        val north = SphericalUtil.computeOffset(center, radiusMeters, 0.0)
                        val east = SphericalUtil.computeOffset(center, radiusMeters, 90.0)
                        val south = SphericalUtil.computeOffset(center, radiusMeters, 180.0)
                        val west = SphericalUtil.computeOffset(center, radiusMeters, 270.0)
                        CameraUpdateFactory.newLatLngBounds(
                            LatLngBounds.builder().include(north).include(east).include(south)
                                .include(west)
                                .build(),
                            24
                        )
                    } else {
                        CameraUpdateFactory.newLatLngZoom(center, 13f)
                    }
                    try {
                        map.animateCamera(update)
                    } catch (_: Exception) {
                        map.moveCamera(update)
                    }
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}





