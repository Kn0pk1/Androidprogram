package com.example.androidprogram.ui.activity

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprogram.R
import com.example.androidprogram.database.ActivityEntity
import com.example.androidprogram.database.ActivityType
import com.example.androidprogram.utils.calculateDistance
import java.util.*
import java.util.concurrent.TimeUnit

class ActivityAdapter(
    private val onActivityClicked: (ActivityEntity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = emptyList<Any>()

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    // --- ViewHolder для заголовка ---
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerTitle: TextView = view.findViewById(R.id.header_title)
    }

    // --- ViewHolder для элемента активности ---
    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val distanceText: TextView = itemView.findViewById(R.id.distance_text)
        val durationText: TextView = itemView.findViewById(R.id.duration_text)
        val activityTypeText: TextView = itemView.findViewById(R.id.activity_type_text)
        val dateText: TextView = itemView.findViewById(R.id.date_text)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val activity = items[position] as ActivityEntity
                    onActivityClicked(activity)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is String -> VIEW_TYPE_HEADER
            is ActivityEntity -> VIEW_TYPE_ITEM
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_date_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_activity, parent, false)
                ActivityViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.headerTitle.text = items[position] as String
            }
            is ActivityViewHolder -> {
                val currentActivity = items[position] as ActivityEntity

                val distanceInMeters = calculateDistance(currentActivity.coordinates)
                holder.distanceText.text = if (distanceInMeters >= 1000) {
                    String.format(Locale.US, "%.2f км", distanceInMeters / 1000)
                } else {
                    String.format(Locale.US, "%.0f м", distanceInMeters)
                }

                holder.durationText.text = formatDuration(currentActivity.endTime - currentActivity.startTime)

                holder.activityTypeText.text = when (currentActivity.activityType) {
                    ActivityType.BICYCLE -> "Велосипед \uD83D\uDEB4"
                    ActivityType.RUNNING -> "Бег \uD83C\uDFC3"
                    ActivityType.STEP -> "Шаг \uD83D\uDEB6"
                }

                val isToday = DateUtils.isToday(currentActivity.startTime)
                val isYesterday = DateUtils.isToday(currentActivity.startTime + DateUtils.DAY_IN_MILLIS)

                holder.dateText.text = if (isToday || isYesterday) {
                    DateUtils.getRelativeTimeSpanString(
                        currentActivity.startTime,
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE
                    )
                } else {
                    java.text.SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(currentActivity.startTime))
                }
            }
        }
    }

    override fun getItemCount() = items.size

    fun setItems(activities: List<Any>) {
        this.items = activities
        notifyDataSetChanged()
    }

    private fun formatDuration(durationMillis: Long): String {
        val totalMinutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis)
        if (totalMinutes < 1) return "меньше минуты"
        if (totalMinutes < 60) return "$totalMinutes мин"
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (minutes == 0L) "$hours ч" else "$hours ч $minutes мин"
    }
} 