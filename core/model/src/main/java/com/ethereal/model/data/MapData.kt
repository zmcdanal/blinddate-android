package com.ethereal.model.data


data class MapData(
    val userLocation: GeoPoint?,
    val center: GeoPoint?,
    val radiusMeters: Float,
    val isEditingRadius: Boolean,
    val isMapReady: Boolean
)