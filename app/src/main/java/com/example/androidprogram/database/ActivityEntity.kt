package com.example.androidprogram.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Enum для типа активности
enum class ActivityType {
    BICYCLE,
    RUNNING,
    STEP
}

// Data class для хранения координат
data class LatLng(
    val latitude: Double,
    val longitude: Double
)

// Entity для таблицы активностей
@Entity(tableName = "user_activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String, // ID пользователя, создавшего активность
    val activityType: ActivityType,
    val startTime: Long,
    val endTime: Long,
    val coordinates: List<LatLng>,
    val comment: String? = null
)

// Конвертеры для сложных типов данных
class Converters {
    @TypeConverter
    fun fromCoordinatesList(coordinates: List<LatLng>?): String? {
        return Gson().toJson(coordinates)
    }

    @TypeConverter
    fun toCoordinatesList(coordinatesJson: String?): List<LatLng>? {
        if (coordinatesJson == null) return null
        val type = object : TypeToken<List<LatLng>>() {}.type
        return Gson().fromJson(coordinatesJson, type)
    }

    @TypeConverter
    fun fromActivityType(activityType: ActivityType?): String? {
        return activityType?.name
    }

    @TypeConverter
    fun toActivityType(activityTypeString: String?): ActivityType? {
        return activityTypeString?.let { ActivityType.valueOf(it) }
    }
} 