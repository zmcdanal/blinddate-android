package com.ethereal.model.data


data class MapData(
    val userLocation: GeoPoint?,
    val cityState: String = "",
    val radiusMiles: Double,
    val isEditingRadius: Boolean,
    val isMapReady: Boolean
)