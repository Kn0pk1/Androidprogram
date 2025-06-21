package com.example.androidprogram.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidprogram.database.ActivityEntity
import com.example.androidprogram.database.AppDatabase
import com.example.androidprogram.UserManager

class MyActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val activityDao = AppDatabase.getDatabase(application).activityDao()
    private val userManager = UserManager(application)

    val allActivities: LiveData<List<ActivityEntity>> = activityDao.getUserActivities(userManager.getCurrentUser()?.login ?: "")
} 