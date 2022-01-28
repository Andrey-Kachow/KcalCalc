package com.andruhavuho.man.like.kcalcalc.ui

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.andruhavuho.man.like.kcalcalc.AddSportActivity
import com.andruhavuho.man.like.kcalcalc.R
import com.andruhavuho.man.like.kcalcalc.models.Sport

class SportOptionsAdapter(
    val context: Context
) : RecyclerView.Adapter<SportOptionsAdapter.ViewHolder>() {

    var sports: List<Sport> = listOf()
    var pickedSport: Sport? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_sport_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sports[position])
        holder.itemView.setOnClickListener {
            holder.getColoredPicked()
            pickedSport = holder.sport
            (context as AddSportActivity).receivePickedSport(pickedSport)
        }
    }

    override fun getItemCount(): Int {
        return sports.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val clSportOptionContainer = itemView.findViewById<ConstraintLayout>(R.id.clSportOptionContainer)
        private val tvSportOptionTitle = itemView.findViewById<TextView>(R.id.tvSportOptionTitle)
        private val tvSportOptionValue = itemView.findViewById<TextView>(R.id.tvSportOptionValue)
        lateinit var sport: Sport

        fun bind(givenSport: Sport) {
            sport = givenSport
            tvSportOptionTitle.text = sport.name
            tvSportOptionValue.text = "${sport.kcalPerHour} ккал в час"
        }

        fun getColoredPicked() {
            val backgroundColorTransitionDrawable = ContextCompat
                .getDrawable(context, R.drawable.transition_option_color_picked)
                    as TransitionDrawable?
            clSportOptionContainer.background = backgroundColorTransitionDrawable
            backgroundColorTransitionDrawable?.startTransition(COLOR_TRANSITION_TIME)
            backgroundColorTransitionDrawable?.reverseTransition(COLOR_TRANSITION_TIME)
        }
    }

    companion object {
        const val COLOR_TRANSITION_TIME = 1000
    }
}