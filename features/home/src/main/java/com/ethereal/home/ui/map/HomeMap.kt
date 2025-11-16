package com.ethereal.home.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ethereal.design.reusables.NeonSpinner
import com.ethereal.design.theme.NeonRose
import com.ethereal.model.data.DateDetails
import com.ethereal.model.utils.milesToMeters
import com.ethereal.model.utils.toLatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun HomeMap(
    dateDetails: DateDetails,
    isMapLoading: Boolean,
    modifier: Modifier = Modifier,
    interactive: Boolean = true,
    allowTapToChooseCenter: Boolean = false,
    onCenterChanged: (LatLng) -> Unit = {}
) {
    val mapData = dateDetails.mapData
    val center = mapData.userLocation?.toLatLng()
    val radiusMeters = milesToMeters(mapData.radiusMiles)

    val cameraState = rememberCameraPositionState()
    val markerState: MarkerState = rememberMarkerState(
        position = center ?: LatLng(0.0, 0.0)
    )

    // Keep marker + camera in sync with domain state
    LaunchedEffect(center?.latitude, center?.longitude, radiusMeters) {
        if (center == null) return@LaunchedEffect

        markerState.position = center

        val update = if (radiusMeters > 1.0) {
            val north = SphericalUtil.computeOffset(center, radiusMeters, 0.0)
            val east = SphericalUtil.computeOffset(center, radiusMeters, 90.0)
            val south = SphericalUtil.computeOffset(center, radiusMeters, 180.0)
            val west = SphericalUtil.computeOffset(center, radiusMeters, 270.0)

            val bounds = LatLngBounds.builder()
                .include(north)
                .include(east)
                .include(south)
                .include(west)
                .build()

            CameraUpdateFactory.newLatLngBounds(bounds, 24)
        } else {
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(center, 13f)
            )
        }

        try {
            cameraState.animate(update)
        } catch (_: Exception) {
            cameraState.move(update)
        }
    }

    Box(modifier) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraState,
            properties = MapProperties(
                isMyLocationEnabled = false,
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                scrollGesturesEnabled = interactive && !isMapLoading,
                zoomGesturesEnabled = interactive && !isMapLoading,
                rotationGesturesEnabled = interactive && !isMapLoading,
                tiltGesturesEnabled = interactive && !isMapLoading
            ),
            onMapClick = { latLng ->
                if (interactive && allowTapToChooseCenter) {
                    // move marker immediately for feedback
                    markerState.position = latLng
                    onCenterChanged(latLng)
                }
            }
        ) {
            if (center != null || allowTapToChooseCenter) {
                Circle(
                    center = markerState.position,
                    radius = maxOf(0.0, radiusMeters),
                    strokeWidth = 5f,
                    strokeColor = NeonRose,
                    fillColor = NeonRose.copy(alpha = 0.2f)
                )

                Marker(
                    state = markerState,
                    title = "Date center"
                )
            }
        }

        if (isMapLoading) {
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                NeonSpinner()
            }
        }
    }
}
