package com.example.androidprogram.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidprogram.R
import com.example.androidprogram.database.ActivityEntity
import com.example.androidprogram.database.ActivityType
import com.example.androidprogram.utils.calculateDistance
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ActivityDetailsFragment : Fragment() {

    private lateinit var viewModel: ActivityDetailsViewModel
    private var activityId: Int = -1
    private var currentActivity: ActivityEntity? = null

    private lateinit var toolbarTitle: TextView
    private lateinit var distanceText: TextView
    private lateinit var dateRelativeText: TextView
    private lateinit var durationText: TextView
    private lateinit var startTimeText: TextView
    private lateinit var finishTimeText: TextView
    private lateinit var commentInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            activityId = it.getInt(ARG_ACTIVITY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity_details, container, false)
        viewModel = ViewModelProvider(this).get(ActivityDetailsViewModel::class.java)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_delete -> {
                    deleteActivity()
                    true
                }
                R.id.action_share -> {
                    shareActivity()
                    true
                }
                else -> false
            }
        }

        toolbarTitle = view.findViewById(R.id.toolbar_title)
        distanceText = view.findViewById(R.id.details_distance)
        dateRelativeText = view.findViewById(R.id.details_date_relative)
        durationText = view.findViewById(R.id.details_duration)
        startTimeText = view.findViewById(R.id.details_start_time)
        finishTimeText = view.findViewById(R.id.details_finish_time)
        commentInput = view.findViewById(R.id.comment_input)

        commentInput.addTextChangedListener {
            currentActivity?.let { activity ->
                viewModel.updateComment(activity, it.toString())
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getActivity(activityId).observe(viewLifecycleOwner) { activity ->
            activity?.let {
                currentActivity = it
                updateUi(it)
            }
        }
    }

    private fun updateUi(activity: ActivityEntity) {
        val activityTitle = when (activity.activityType) {
            ActivityType.BICYCLE -> "Велосипед \uD83D\uDEB4"
            ActivityType.RUNNING -> "Бег \uD83C\uDFC3"
            ActivityType.STEP -> "Шаг \uD83D\uDEB6"
        }
        toolbarTitle.text = activityTitle

        val distanceInMeters = calculateDistance(activity.coordinates)
        distanceText.text = if (distanceInMeters >= 1000) {
            String.format(Locale.US, "%.2f км", distanceInMeters / 1000)
        } else {
            String.format(Locale.US, "%.0f м", distanceInMeters)
        }

        dateRelativeText.text = DateUtils.getRelativeTimeSpanString(
            activity.startTime, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS
        )

        val durationMillis = activity.endTime - activity.startTime
        durationText.text = formatDuration(durationMillis)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        startTimeText.text = "Старт ${timeFormat.format(Date(activity.startTime))}"
        finishTimeText.text = "Финиш ${timeFormat.format(Date(activity.endTime))}"

        if (commentInput.text.toString() != activity.comment) {
            commentInput.setText(activity.comment)
        }
    }

    private fun deleteActivity() {
        currentActivity?.let {
            viewModel.deleteActivity(it)
            parentFragmentManager.popBackStack()
        }
    }

    private fun shareActivity() {
        currentActivity?.let {
            val shareText = "Я занимался! (${toolbarTitle.text}). " +
                    "Пробежал ${distanceText.text} за ${durationText.text}. "
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
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
        private const val ARG_ACTIVITY_ID = "activity_id"

        @JvmStatic
        fun newInstance(activityId: Int) =
            ActivityDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ACTIVITY_ID, activityId)
                }
            }
    }
}
// Helper extension in ActivityAdapter to reuse it.
/* fun ActivityAdapter.calculateDistance(coordinates: List<com.example.androidprogram.database.LatLng>): Float {
    var totalDistance = 0f
    if (coordinates.size > 1) {
        for (i in 0 until coordinates.size - 1) {
            val startPoint = android.location.Location("startPoint").apply {
                latitude = coordinates[i].latitude
                longitude = coordinates[i].longitude
            }
            val endPoint = android.location.Location("endPoint").apply {
                latitude = coordinates[i + 1].latitude
                longitude = coordinates[i + 1].longitude
            }
            totalDistance += startPoint.distanceTo(endPoint)
        }
    }
    return totalDistance
} */ 