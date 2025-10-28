package com.ethereal.model.utils

import com.ethereal.model.data.GeoPoint
import com.google.android.gms.maps.model.LatLng


private const val METERS_PER_MILE = 1609.344f
fun milesToMeters(mi: Double): Double = mi * METERS_PER_MILE
fun metersToMiles(m: Double): Double = m / METERS_PER_MILE

fun GeoPoint.toLatLng(): LatLng = LatLng(lat, lng)