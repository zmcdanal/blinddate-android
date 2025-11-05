package com.ethereal.model.data


data class DateDetails(
    val date: String = "",
    val cuisine: String = "",
    val keywords: List<String> = emptyList(),
    val priceLevel: Int = 2,
    val fastFood: Boolean = false,
    val mapData: MapData
)

data class MapData(
    val userLocation: GeoPoint?,
    val cityState: String = "",
    val radiusMiles: Double
)