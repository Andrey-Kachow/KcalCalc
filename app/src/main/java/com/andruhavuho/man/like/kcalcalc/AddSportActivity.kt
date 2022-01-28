package com.andruhavuho.man.like.kcalcalc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.andruhavuho.man.like.kcalcalc.contracts.*
import com.andruhavuho.man.like.kcalcalc.models.Sport
import com.andruhavuho.man.like.kcalcalc.models.SportTime
import com.andruhavuho.man.like.kcalcalc.ui.SportOptionsAdapter
import com.andruhavuho.man.like.kcalcalc.ui.TextChangedListener
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class AddSportActivity : AppCompatActivity() {

    private var pickedSport: Sport? = null

    private lateinit var btnCancelAddingSport: Button
    private lateinit var btnConfirmAddingSport: Button
    private lateinit var etNumMinutes: EditText
    private lateinit var etNumHours: EditText
    private lateinit var tvChosenActivity: TextView
    private lateinit var clAddSportRoot: ConstraintLayout

    private lateinit var sportOptionsAdapter: SportOptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sport)

        if (savedInstanceState != null) {
            pickedSport = savedInstanceState.getParcelable(SAVED_PICKED_SPORT)
        }

        setupPickedSportsRecyclerView()
        initializeViews()

        btnConfirmAddingSport.setOnClickListener {
            if (sportIsPicked() && minutesArePicked() && hoursArePicked()) {
                confirmSportAndReturnToCalc()
            } else {
                indicateThatAllFieldsMustBeFilled()
            }
        }
        btnCancelAddingSport.setOnClickListener {
            onBackPressed()
        }
        etNumHours.addTextChangedListener(object : TextChangedListener<EditText?>(etNumHours) {
            override fun onTextChanged(target: EditText?, s: Editable?) {
                onTimeTextChanged(target, s)
                if (getEnteredHours() ?: 0 > MAX_HOURS) {
                    target?.setText("$MAX_HOURS", TextView.BufferType.EDITABLE)
                }
            }
        })
        etNumMinutes.addTextChangedListener(object : TextChangedListener<EditText?>(etNumMinutes) {
            override fun onTextChanged(target: EditText?, s: Editable?) {
                onTimeTextChanged(target, s)
                if (getEnteredHours() ?: 0 > MAX_MINUTES) {
                    target?.setText("$MAX_MINUTES", TextView.BufferType.EDITABLE)
                }
            }
        })
        sportOptionsAdapter.sports = extractSportsFromJSON()
        sportOptionsAdapter.notifyDataSetChanged()
    }

    private fun initializeViews() {
        btnConfirmAddingSport = findViewById(R.id.btnConfirmAddingSport)
        btnCancelAddingSport = findViewById(R.id.btnCancelAddingSport)
        tvChosenActivity = findViewById(R.id.tvChosenActivity)
        clAddSportRoot = findViewById(R.id.clAddSportRoot)
        etNumMinutes = findViewById(R.id.etNumMinutes)
        etNumHours = findViewById(R.id.etNumHours)
    }

    override fun onSaveInstanceState(icicle: Bundle) {
        super.onSaveInstanceState(icicle)
        icicle.putParcelable(SAVED_PICKED_SPORT, pickedSport)
    }

    fun onTimeTextChanged(target: EditText?, s: Editable?) {
        target?.setTextColor(ContextCompat.getColor(this, R.color.green_dark_text))
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back)
    }

    fun receivePickedSport(sport: Sport?) {
        if (sport != null) {
            pickedSport = sport
            tvChosenActivity.text = "Выбрано: \"${sport.name}\""
            tvChosenActivity.setTextColor(ContextCompat.getColor(this, R.color.green_dark_text))
        } else {
            Snackbar.make(clAddSportRoot, "Something went wrong", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun confirmSportAndReturnToCalc() {
        val result = Intent().apply {
            putExtra(PICKED_SPORT_ID, pickedSport?.id ?: -1)
            putExtra(PICKED_SPORT_NAME, pickedSport?.name ?: "Error")
            putExtra(PICKED_SPORT_KCAL_PER_HOUR, pickedSport?.kcalPerHour ?: -1)
            putExtra(PICKED_SPORT_NUM_HOURS, getEnteredHours() ?: -1)
            putExtra(PICKED_SPORT_NUM_MINUTES, getEnteredMinutes() ?: -1)
        }
        setResult(Activity.RESULT_OK, result)
        finish()
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back)
    }

    private fun getEnteredHours(): Int? {
        return etNumHours.text.toString().toIntOrNull()
    }

    private fun getEnteredMinutes(): Int? {
        return etNumMinutes.text.toString().toIntOrNull()
    }

    private fun setupPickedSportsRecyclerView() {
        sportOptionsAdapter = SportOptionsAdapter(this)
        val rvAllSportsOptions = findViewById<RecyclerView>(R.id.rvAllSportsOptions)
        rvAllSportsOptions.adapter = sportOptionsAdapter
    }

    private fun indicateThatAllFieldsMustBeFilled() {
        // Vibrate
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATION_TIME, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v.vibrate(VIBRATION_TIME)
        }
        if (!sportIsPicked()) {
            // Red Color
            tvChosenActivity.setTextColor(Color.RED)
            // Shake label
            tvChosenActivity.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
        }
        if (!hoursArePicked()) {
            etNumHours.setHintTextColor(Color.RED)
        }
        if (!minutesArePicked()) {
            etNumMinutes.setHintTextColor(Color.RED)
        }
    }

    private fun sportIsPicked(): Boolean {
        return pickedSport != null
    }

    private fun hoursArePicked(): Boolean {
        return !etNumHours.text.isNullOrBlank()
    }

    private fun minutesArePicked(): Boolean {
        return !etNumMinutes.text.isNullOrBlank()
    }

    private fun extractSportsFromJSON(): List<Sport> {
        return try {
            val obj = JSONObject(loadJSONFromAsset())
            val sports = obj.getJSONArray("sports")
            val sportList = mutableListOf<Sport>()
            for (i in 0 until sports.length()) {
                val sport = sports.getJSONObject(i)
                val id = sport.getLong("id")
                val sportName = sport.getString("name")
                val kcalPerHour = sport.getInt("kcalPerHour")
                sportList.add(Sport(id, sportName, kcalPerHour))
            }
            sportList
        } catch (e: JSONException) {
            e.printStackTrace()
            listOf()
        }
    }

    private fun loadJSONFromAsset(): String {
        val jsonString: String? = try {
            val stream: InputStream = assets.open("sports.json")
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            String(buffer, charset("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            return EMPTY_SPORTS_JSON_STRING
        }
        return jsonString ?: EMPTY_SPORTS_JSON_STRING
    }

    companion object {
        const val EMPTY_SPORTS_JSON_STRING = "{\"sports\": []}"
        const val SAVED_PICKED_SPORT = "SAVED_PICKED_SPORT"
        const val MAX_MINUTES = 59
        const val MAX_HOURS = 23
        const val VIBRATION_TIME = 500L
    }
}