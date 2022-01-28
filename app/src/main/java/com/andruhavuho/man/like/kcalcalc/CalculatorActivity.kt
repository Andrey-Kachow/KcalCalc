package com.andruhavuho.man.like.kcalcalc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.andruhavuho.man.like.kcalcalc.contracts.AddSportContract
import com.andruhavuho.man.like.kcalcalc.models.SportTime
import com.andruhavuho.man.like.kcalcalc.ui.PickedSportsAdapter

class CalculatorActivity : AppCompatActivity() {

    private lateinit var etWeight: EditText
    private lateinit var etAge: EditText
    private lateinit var etHeight: EditText

    private lateinit var pickedSportsAdapter: PickedSportsAdapter

    private val activityLauncher = registerForActivityResult(AddSportContract()) { resultSP ->
        if (resultSP != null) {
            pickedSportsAdapter.sports.add(resultSP)
            pickedSportsAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        var pickedSports: MutableList<SportTime>? = null
        if (savedInstanceState != null){
            pickedSports = savedInstanceState
                .getParcelableArray(SAVED_PICKED_SPORTS)
                ?.map { it as SportTime } as MutableList?
        }

        setupPickedSportsRecyclerView()

        etWeight = findViewById(R.id.etWeight)
        etAge = findViewById(R.id.etAge)
        etHeight = findViewById(R.id.etHeight)

        findViewById<ImageButton>(R.id.ibAddSport).setOnClickListener {
            goToAddSportActivity()
        }
        findViewById<Button>(R.id.btnCalculateCalories).setOnClickListener {
            if (weightIsEntered() && ageIsEntered() && heightIsEntered()) {
                calculateCaloriesAndDisplayTheResult()
            } else {
                showIncompleteFormWarningDialog()
            }
        }
        findViewById<ImageView>(R.id.ivInfoIcon).setOnClickListener {
            showInfoDialog()
        }
        pickedSportsAdapter.sports = pickedSports ?: mutableListOf()
        pickedSportsAdapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(icicle: Bundle) {
        super.onSaveInstanceState(icicle)
        icicle.putParcelableArray(SAVED_PICKED_SPORTS, pickedSportsAdapter.sports.toTypedArray())
    }

    private fun calculateCaloriesAndDisplayTheResult() {
        val w = getEnteredWeight()
        val t = getEnteredAge()
        val h = getEnteredHeight()
        if (w == null || t == null || h == null) {
            return
        }
        val baseMetabolicRate = 80 + (13 * w) + (4 * h) - (5 * t)
        val totalCalories = baseMetabolicRate.toInt() + pickedSportsAdapter.getTotalCalories()
        AlertDialog.Builder(this)
            .setTitle("За день было потрачено ${totalCalories}ккал")
            .setPositiveButton("OK") { _, _ ->
            }.show()
    }

    private fun showIncompleteFormWarningDialog() {
        val warningView = LayoutInflater.from(this).inflate(R.layout.dialog_incomplete_form_warning, null)
        showAlertDialog("Недостаточно данных", warningView) {}
    }

    private fun showInfoDialog() {
        val calcInfoView = LayoutInflater.from(this).inflate(R.layout.dialog_calc_info, null)
        showAlertDialog("Как это работает", calcInfoView) {}
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    override fun onBackPressed() {
//        finish()
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    private fun goToAddSportActivity() {
        activityLauncher.launch(Unit)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    private fun setupPickedSportsRecyclerView() {
        pickedSportsAdapter = PickedSportsAdapter()
        val rvUserPickedSportsActivities = findViewById<RecyclerView>(R.id.rvUserPickedSportsActivities)
        rvUserPickedSportsActivities.adapter = pickedSportsAdapter
    }

    private fun weightIsEntered(): Boolean {
        return !etWeight.text.isNullOrBlank()
    }

    private fun ageIsEntered(): Boolean {
        return !etAge.text.isNullOrBlank()
    }

    private fun heightIsEntered(): Boolean {
        return !etHeight.text.isNullOrBlank()
    }

    private fun getEnteredWeight(): Double? {
        return etWeight.text.toString().toDoubleOrNull()
    }

    private fun getEnteredAge(): Int? {
        return etAge.text.toString().toIntOrNull()
    }

    private fun getEnteredHeight(): Int? {
        return etHeight.text.toString().toIntOrNull()
    }

    companion object {
        const val SAVED_PICKED_SPORTS = "SAVED_PICKED_SPORTS"
    }
}