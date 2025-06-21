package com.example.androidprogram.ui.activity.tabs

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprogram.R
import com.example.androidprogram.database.ActivityEntity
import com.example.androidprogram.database.ActivityType
import com.example.androidprogram.model.CommunityActivity
import com.example.androidprogram.ui.activity.CommunityActivityDetailsFragment
import com.example.androidprogram.ui.activity.CommunityActivityViewModel
import com.example.androidprogram.utils.calculateDistance
import java.text.SimpleDateFormat
import java.util.*

class UsersActivityFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommunityActivityAdapter
    private lateinit var communityViewModel: CommunityActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users_activity, container, false)

        setupRecyclerView(view)

        communityViewModel = ViewModelProvider(this).get(CommunityActivityViewModel::class.java)
        communityViewModel.allActivities.observe(viewLifecycleOwner) { activities ->
            val communityActivities = activities.map { mapActivityEntityToCommunityActivity(it) }
            val groupedActivities = groupActivitiesByDate(communityActivities)
            adapter.setItems(groupedActivities)
        }

        return view
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.users_recyclerview)
        adapter = CommunityActivityAdapter { selectedActivity ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, CommunityActivityDetailsFragment.newInstance(selectedActivity))
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun mapActivityEntityToCommunityActivity(entity: ActivityEntity): CommunityActivity {
        val activityName = when (entity.activityType) {
            ActivityType.BICYCLE -> "Велосипед"
            ActivityType.RUNNING -> "Бег"
            ActivityType.STEP -> "Шаг"
        }
        val distance = calculateDistance(entity.coordinates)
        val duration = entity.endTime - entity.startTime

        return CommunityActivity(
            id = entity.id,
            userHandle = entity.userId,
            activityName = activityName,
            activityType = entity.activityType,
            distanceMeters = distance,
            durationMillis = duration,
            timestamp = entity.startTime,
            comment = entity.comment ?: ""
        )
    }

    private fun groupActivitiesByDate(activities: List<CommunityActivity>): List<Any> {
        val groupedList = mutableListOf<Any>()
        if (activities.isEmpty()) return groupedList

        val sortedActivities = activities.sortedByDescending { it.timestamp }

        var lastHeader = ""
        sortedActivities.forEach { activity ->
            val header = getHeaderForTimestamp(activity.timestamp)
            if (header != lastHeader) {
                groupedList.add(header)
                lastHeader = header
            }
            groupedList.add(activity)
        }
        return groupedList
    }

    private fun getHeaderForTimestamp(timestamp: Long): String {
        return when {
            DateUtils.isToday(timestamp) -> "Сегодня"
            DateUtils.isToday(timestamp + DateUtils.DAY_IN_MILLIS) -> "Вчера"
            else -> {
                val format = SimpleDateFormat("LLLL yyyy", Locale("ru"))
                format.format(Date(timestamp)).replaceFirstChar { it.titlecase(Locale("ru")) }
            }
        }
    }
} 