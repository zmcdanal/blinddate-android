package com.ethereal.home.location.geo

import com.ethereal.model.data.GeoPoint
import com.google.android.gms.maps.model.LatLng

fun GeoPoint.toLatLng(): LatLng = LatLng(lat, lng)