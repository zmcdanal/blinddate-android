package com.ethereal.model.data

data class DateDetails(
    val genre: String,
    val keywords: List<String>,
    val priceLevel: Int,
    val fastFood: Boolean,
    val mapData: MapData
)