package com.example.androidprogram.model

import android.os.Parcelable
import com.example.androidprogram.database.ActivityType
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityActivity(
    val id: Int,
    val userHandle: String,
    val activityName: String,
    val activityType: ActivityType,
    val distanceMeters: Float,
    val durationMillis: Long,
    val timestamp: Long,
    val comment: String
) : Parcelable 