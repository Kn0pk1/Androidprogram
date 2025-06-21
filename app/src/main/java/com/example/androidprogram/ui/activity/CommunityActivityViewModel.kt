package com.example.androidprogram.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidprogram.database.ActivityEntity
import com.example.androidprogram.database.AppDatabase

class CommunityActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val activityDao = AppDatabase.getDatabase(application).activityDao()

    val allActivities: LiveData<List<ActivityEntity>> = activityDao.getAllActivities()
} 