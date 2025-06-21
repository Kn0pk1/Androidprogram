package com.example.androidprogram.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidprogram.database.ActivityDao
import com.example.androidprogram.database.ActivityEntity
import com.example.androidprogram.database.AppDatabase
import kotlinx.coroutines.launch

class ActivityDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val activityDao: ActivityDao = AppDatabase.getDatabase(application).activityDao()

    fun getActivity(id: Int): LiveData<ActivityEntity> {
        return activityDao.getActivityById(id)
    }

    fun updateComment(activity: ActivityEntity, newComment: String) {
        viewModelScope.launch {
            if (activity.comment != newComment) {
                val updatedActivity = activity.copy(comment = newComment)
                activityDao.updateActivity(updatedActivity)
            }
        }
    }

    fun deleteActivity(activity: ActivityEntity) {
        viewModelScope.launch {
            activityDao.deleteActivity(activity)
        }
    }
} 