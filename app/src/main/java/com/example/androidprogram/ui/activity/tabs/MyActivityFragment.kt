package com.example.androidprogram.ui.activity.tabs

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprogram.R
import com.example.androidprogram.database.ActivityEntity
import com.example.androidprogram.ui.activity.ActivityAdapter
import com.example.androidprogram.ui.activity.ActivityDetailsFragment
import com.example.androidprogram.ui.activity.MyActivityViewModel
import java.text.SimpleDateFormat
import java.util.*

class MyActivityFragment : Fragment() {

    private lateinit var myActivityViewModel: MyActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private val adapter by lazy {
        ActivityAdapter { activity ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, ActivityDetailsFragment.newInstance(activity.id))
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_activity, container, false)

        recyclerView = view.findViewById(R.id.recyclerview)
        emptyStateLayout = view.findViewById(R.id.empty_state_layout)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        myActivityViewModel = ViewModelProvider(this).get(MyActivityViewModel::class.java)

        myActivityViewModel.allActivities.observe(viewLifecycleOwner) { activities ->
            if (activities.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                emptyStateLayout.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyStateLayout.visibility = View.GONE
                adapter.setItems(groupActivitiesByDate(activities))
            }
        }

        return view
    }

    private fun groupActivitiesByDate(activities: List<ActivityEntity>): List<Any> {
        val groupedList = mutableListOf<Any>()
        if (activities.isEmpty()) return groupedList

        var lastHeader = ""
        activities.forEach { activity ->
            val header = getHeaderForTimestamp(activity.startTime)
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