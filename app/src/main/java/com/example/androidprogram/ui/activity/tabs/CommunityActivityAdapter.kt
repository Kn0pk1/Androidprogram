package com.example.androidprogram.ui.activity.tabs

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprogram.R
import com.example.androidprogram.model.CommunityActivity
import java.util.*
import java.util.concurrent.TimeUnit

class CommunityActivityAdapter(
    private val onActivityClicked: (CommunityActivity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Any> = emptyList()

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerTitle: TextView = view.findViewById(R.id.header_title)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val distanceText: TextView = itemView.findViewById(R.id.community_distance_text)
        val userHandleText: TextView = itemView.findViewById(R.id.community_user_handle)
        val durationText: TextView = itemView.findViewById(R.id.community_duration_text)
        val activityNameText: TextView = itemView.findViewById(R.id.community_activity_name)
        val dateText: TextView = itemView.findViewById(R.id.community_date_text)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && items[position] is CommunityActivity) {
                    onActivityClicked(items[position] as CommunityActivity)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_date_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_community_activity, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is HeaderViewHolder -> {
                holder.headerTitle.text = items[position] as String
            }
            is ViewHolder -> {
                val activity = items[position] as CommunityActivity
                if (activity.distanceMeters >= 1000) {
                    holder.distanceText.text = String.format(Locale.US, "%.2f км", activity.distanceMeters / 1000)
                } else {
                    holder.distanceText.text = String.format(Locale.US, "%.0f м", activity.distanceMeters)
                }
                holder.userHandleText.text = activity.userHandle
                holder.durationText.text = formatDuration(activity.durationMillis)
                holder.activityNameText.text = activity.activityName
                holder.dateText.text = DateUtils.getRelativeTimeSpanString(
                    activity.timestamp, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS
                )
            }
        }
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<Any>) {
        items = newItems
        notifyDataSetChanged()
    }

    private fun formatDuration(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val text = mutableListOf<String>()
        if (hours > 0) text.add("$hours " + if (hours == 1L) "час" else "часа")
        if (minutes > 0) text.add("$minutes " + if (minutes == 1L) "минута" else "минут")
        return text.joinToString(" ")
    }
} 