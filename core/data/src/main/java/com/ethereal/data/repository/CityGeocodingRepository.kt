package com.ethereal.data.repository

import com.ethereal.model.data.GeoPoint

interface CityGeocodingRepository {
    suspend fun cityToLatLng(query: String): GeoPoint?
}