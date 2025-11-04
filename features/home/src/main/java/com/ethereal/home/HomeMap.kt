package com.ethereal.home

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.ethereal.design.theme.NeonRose
import com.ethereal.model.data.DateDetails
import com.ethereal.model.utils.milesToMeters
import com.ethereal.model.utils.toLatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.*

@Composable
fun HomeMap(
    dateDetails: DateDetails,
    modifier: Modifier = Modifier,
    interactive: Boolean = true
) {
    val mapData = dateDetails.mapData
    val center: LatLng? = mapData.userLocation?.toLatLng()
    val radiusMeters = milesToMeters(mapData.radiusMiles)

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center ?: LatLng(37.42, -122.084), 13f)
    }

    // Convert a comfy 24dp padding to pixels for bounds
    val paddingPx = with(LocalDensity.current) { 24f }.toInt()

    // Whenever center or radius changes, fit the bounds
    LaunchedEffect(center, radiusMeters) {
        if (center != null && radiusMeters > 0.0) {
            // Four cardinal points on the circle (N,E,S,W)
            val north = SphericalUtil.computeOffset(center, radiusMeters, 0.0)
            val east = SphericalUtil.computeOffset(center, radiusMeters, 90.0)
            val south = SphericalUtil.computeOffset(center, radiusMeters, 180.0)
            val west = SphericalUtil.computeOffset(center, radiusMeters, 270.0)

            val bounds = LatLngBounds.builder()
                .include(north).include(east).include(south).include(west)
                .build()

            val update = CameraUpdateFactory.newLatLngBounds(bounds, paddingPx)
            cameraState.animate(update)
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraState,
        properties = MapProperties(
            isMyLocationEnabled = false,
            mapType = MapType.NORMAL
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
            scrollGesturesEnabled = interactive,
            zoomGesturesEnabled = interactive,
            rotationGesturesEnabled = interactive,
            tiltGesturesEnabled = interactive
        )
    ) {
        center?.let {
            Circle(
                center = it,
                radius = radiusMeters,
                strokeWidth = 5f,
                strokeColor = NeonRose,
                fillColor = NeonRose.copy(alpha = 0.2f)
            )

            mapData.userLocation?.toLatLng()?.let { pos ->
                Marker(state = MarkerState(pos), title = "You")
            }
        }
    }
}
