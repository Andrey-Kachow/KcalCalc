package com.andruhavuho.man.like.kcalcalc.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.andruhavuho.man.like.kcalcalc.AddSportActivity
import com.andruhavuho.man.like.kcalcalc.models.Sport
import com.andruhavuho.man.like.kcalcalc.models.SportTime

class AddSportContract : ActivityResultContract<Unit, SportTime?>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, AddSportActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): SportTime? = when {
        resultCode != Activity.RESULT_OK -> null
        else -> SportTime(
            sport = Sport(
                id = intent?.getLongExtra(PICKED_SPORT_ID, DEFAULT_ID) ?: DEFAULT_ID,
                name = intent?.getStringExtra(PICKED_SPORT_NAME) ?: "NO NAME",
                kcalPerHour = intent?.getIntExtra(PICKED_SPORT_KCAL_PER_HOUR, DEFAULT_MINUS_ONE)
                    ?: DEFAULT_MINUS_ONE,
            ),
            numHours = intent?.getIntExtra(PICKED_SPORT_NUM_HOURS, DEFAULT_MINUS_ONE) ?: DEFAULT_MINUS_ONE,
            numMinutes = intent?.getIntExtra(PICKED_SPORT_NUM_MINUTES, DEFAULT_MINUS_ONE) ?: DEFAULT_MINUS_ONE,
        )
    }

    companion object {
        const val DEFAULT_ID = -1L
        const val DEFAULT_MINUS_ONE = -1
    }
}