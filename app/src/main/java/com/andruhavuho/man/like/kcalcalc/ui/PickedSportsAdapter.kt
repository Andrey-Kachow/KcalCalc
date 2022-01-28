package com.andruhavuho.man.like.kcalcalc.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andruhavuho.man.like.kcalcalc.R
import com.andruhavuho.man.like.kcalcalc.models.Sport
import com.andruhavuho.man.like.kcalcalc.models.SportTime

class PickedSportsAdapter(
//    private val clickListener: (sport: Sport) -> Unit
) : RecyclerView.Adapter<PickedSportsAdapter.ViewHolder>() {

    var sports: MutableList<SportTime> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_picked_sport, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            ivPickedSportDeleteIcon.setOnClickListener {
//                deleteItem(position)         old
                deleteItem(adapterPosition) // new
            }
        }.bind(sports[position])
    }

    override fun getItemCount(): Int {
        return sports.size
    }

    fun getTotalCalories(): Int = sports.map { it.getTotalEnergy() }.sum()

    private fun deleteItem(position: Int) {
        sports.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvPickedSportTitle = itemView.findViewById<TextView>(R.id.tvPickedSportTitle)
        private val tvPickedSportSummary = itemView.findViewById<TextView>(R.id.tvPickedSportSummary)
        val ivPickedSportDeleteIcon: ImageView = itemView.findViewById(R.id.ivPickedSportDeleteIcon)

        fun bind(sportTime: SportTime) {
            tvPickedSportTitle.text = sportTime.sport.name
            tvPickedSportSummary.text = "${sportTime.numHours}ч ${sportTime.numMinutes}мин ${sportTime.getTotalEnergy()}ккал"
        }
    }

    companion object {
        const val NUM_MINUTES_ONE_HOUR = 60
    }
}