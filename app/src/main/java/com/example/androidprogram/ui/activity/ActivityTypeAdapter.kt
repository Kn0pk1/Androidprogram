package com.example.androidprogram.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprogram.R
import com.example.androidprogram.database.ActivityType

data class ActivityTypeItem(val name: String, val type: ActivityType)

class ActivityTypeAdapter(
    private val activityTypes: List<ActivityTypeItem>,
    private val onTypeSelected: (ActivityType) -> Unit
) : RecyclerView.Adapter<ActivityTypeAdapter.ActivityTypeViewHolder>() {

    private var selectedPosition = 0

    inner class ActivityTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.activity_type_name)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val previouslySelectedPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previouslySelectedPosition)
                    notifyItemChanged(selectedPosition)
                    onTypeSelected(activityTypes[selectedPosition].type)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTypeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_activity_type, parent, false)
        return ActivityTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityTypeViewHolder, position: Int) {
        holder.nameTextView.text = activityTypes[position].name
        holder.itemView.isSelected = selectedPosition == position
    }

    override fun getItemCount(): Int = activityTypes.size

    fun getSelectedActivityType(): ActivityType {
        return activityTypes[selectedPosition].type
    }
} 