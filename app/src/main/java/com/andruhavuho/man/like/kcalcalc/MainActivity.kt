package com.andruhavuho.man.like.kcalcalc

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var x1: Float = 0.0F
    private var x2: Float = 0.0F
    private var y1: Float = 0.0F
    private var y2: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                y2 = event.y
                if (x1 < x2) {
                    val intent = Intent(this, CalculatorActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                }
            }
        }
        return false
    }
}