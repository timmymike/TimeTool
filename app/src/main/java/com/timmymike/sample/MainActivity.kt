package com.timmymike.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.timmymike.timetool.TimeUnits
import com.timmymike.viewtool.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).apply {
            isClickable = true
            setTextSize(50)
            clickWithTrigger { isSelected = !isSelected }
            setClickTextColorStateById(R.color.black, R.color.purple_700, R.color.teal_200)
            setClickBgStateById(R.color.teal_700)
        }
    }
}