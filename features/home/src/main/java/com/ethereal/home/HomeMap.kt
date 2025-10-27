package com.ethereal.home


import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ethereal.design.theme.NeonRose
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.ethereal.model.data.MapData
import com.ethereal.home.location.geo.toLatLng
import com.google.android.gms.maps.CameraUpdateFactory

@Composable
fun HomeMap(
    homeScreenUiState: HomeScreenUiState.Ready,
    modifier: Modifier = Modifier,
    interactive: Boolean = false
) {
    val mapData = homeScreenUiState.dateDetails.mapData
    val center = mapData.center?.toLatLng()
    println("hey bobby lat: ${center?.latitude} long: ${center?.longitude}")
    val cameraState = rememberCameraPositionState {
        center?.let { position = CameraPosition.fromLatLngZoom(it, 30f) }
    }


    LaunchedEffect(center) {
        center?.let { cameraState.animate(CameraUpdateFactory.newLatLngZoom(it, 13f)) }
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
        // Radius circle (centered on mapData.center)
        center?.let {
            Circle(
                center = it,
                radius = 2_000.00,
                strokeWidth = 5f,
                strokeColor = NeonRose,
                fillColor = Color.Transparent
            )
        }

        mapData.userLocation?.toLatLng()?.let {
            Marker(state = MarkerState(position = it), title = "You")
        }
    }
}