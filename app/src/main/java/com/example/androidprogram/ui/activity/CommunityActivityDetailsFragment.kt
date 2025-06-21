package com.example.androidprogram.ui.activity

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.androidprogram.R
import com.example.androidprogram.database.ActivityType
import com.example.androidprogram.model.CommunityActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CommunityActivityDetailsFragment : Fragment() {

    private var communityActivity: CommunityActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            communityActivity = it.getParcelable(ARG_ACTIVITY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_community_activity_details, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communityActivity?.let { updateUi(view, it) }
    }

    private fun updateUi(view: View, activity: CommunityActivity) {
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbar_title)
        val userHandle = view.findViewById<TextView>(R.id.details_user_handle)
        val distance = view.findViewById<TextView>(R.id.details_distance)
        val dateRelative = view.findViewById<TextView>(R.id.details_date_relative)
        val duration = view.findViewById<TextView>(R.id.details_duration)
        val startTime = view.findViewById<TextView>(R.id.details_start_time)
        val finishTime = view.findViewById<TextView>(R.id.details_finish_time)
        val comment = view.findViewById<TextView>(R.id.comment_text)

        toolbarTitle.text = when (activity.activityType) {
            ActivityType.BICYCLE -> "Велосипед \uD83D\uDEB4"
            ActivityType.RUNNING -> "Бег \uD83C\uDFC3"
            ActivityType.STEP -> "Шаг \uD83D\uDEB6"
        }
        userHandle.text = activity.userHandle
        distance.text = if (activity.distanceMeters >= 1000) {
            String.format(Locale.US, "%.2f км", activity.distanceMeters / 1000)
        } else {
            String.format(Locale.US, "%.0f м", activity.distanceMeters)
        }
        dateRelative.text = DateUtils.getRelativeTimeSpanString(
            activity.timestamp, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS
        )
        duration.text = formatDuration(activity.durationMillis)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val finishTimestamp = activity.timestamp + activity.durationMillis
        startTime.text = "Старт ${timeFormat.format(Date(activity.timestamp))}"
        finishTime.text = "Финиш ${timeFormat.format(Date(finishTimestamp))}"
        comment.text = activity.comment
    }

    private fun formatDuration(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        return if (hours > 0) {
            "${hours} ч ${minutes} мин"
        } else {
            "${minutes} мин"
        }
    }

    companion object {
        private const val ARG_ACTIVITY = "community_activity"

        @JvmStatic
        fun newInstance(activity: CommunityActivity) =
            CommunityActivityDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ACTIVITY, activity)
                }
            }
    }
} 