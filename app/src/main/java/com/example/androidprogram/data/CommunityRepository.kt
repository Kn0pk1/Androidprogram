package com.example.androidprogram.data

import com.example.androidprogram.database.ActivityType
import com.example.androidprogram.model.CommunityActivity
import java.util.concurrent.TimeUnit

object CommunityRepository {

    fun getCommunityActivities(): List<CommunityActivity> {
        val now = System.currentTimeMillis()
        return listOf(
            CommunityActivity(
                id = 1,
                userHandle = "@van_darkholme",
                activityName = "Серфинг",
                activityType = ActivityType.BICYCLE,
                distanceMeters = 14320f,
                durationMillis = TimeUnit.HOURS.toMillis(2) + TimeUnit.MINUTES.toMillis(46),
                timestamp = now - TimeUnit.HOURS.toMillis(14),
                comment = "Я бежал очень сильно, ты так не сможешь"
            ),
            CommunityActivity(
                id = 2,
                userHandle = "@techniquepasha",
                activityName = "Качели",
                activityType = ActivityType.RUNNING,
                distanceMeters = 228f,
                durationMillis = TimeUnit.HOURS.toMillis(14) + TimeUnit.MINUTES.toMillis(48),
                timestamp = now - TimeUnit.HOURS.toMillis(14),
                comment = "Было легко, как никогда."
            ),
            CommunityActivity(
                id = 3,
                userHandle = "@morgen_shtern",
                activityName = "Езда на кадилак",
                activityType = ActivityType.STEP,
                distanceMeters = 10000f,
                durationMillis = TimeUnit.HOURS.toMillis(1) + TimeUnit.MINUTES.toMillis(10),
                timestamp = now - TimeUnit.HOURS.toMillis(14),
                comment = "Новый трек скоро..."
            )
        )
    }
} 