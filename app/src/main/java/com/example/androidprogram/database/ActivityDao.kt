package com.example.androidprogram.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    @Query("SELECT * FROM user_activities ORDER BY startTime DESC")
    fun getAllActivities(): LiveData<List<ActivityEntity>>

    @Query("SELECT * FROM user_activities WHERE userId = :userId ORDER BY startTime DESC")
    fun getUserActivities(userId: String): LiveData<List<ActivityEntity>>

    @Query("SELECT * FROM user_activities WHERE id = :activityId")
    fun getActivityById(activityId: Int): LiveData<ActivityEntity>

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)
} 