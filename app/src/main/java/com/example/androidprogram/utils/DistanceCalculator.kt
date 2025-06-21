package com.example.androidprogram.utils

import android.location.Location
import com.example.androidprogram.database.LatLng

fun calculateDistance(coordinates: List<LatLng>): Float {
    var totalDistance = 0f
    if (coordinates.size > 1) {
        for (i in 0 until coordinates.size - 1) {
            val startPoint = Location("startPoint").apply {
                latitude = coordinates[i].latitude
                longitude = coordinates[i].longitude
            }
            val endPoint = Location("endPoint").apply {
                latitude = coordinates[i + 1].latitude
                longitude = coordinates[i + 1].longitude
            }
            totalDistance += startPoint.distanceTo(endPoint)
        }
    }
    return totalDistance
} 