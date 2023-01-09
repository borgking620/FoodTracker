package com.smonhof.foodtracker.data

import android.content.Context
import android.util.Log
import java.time.LocalDate

object DataProvider {

    private lateinit var currentDay : EatingDay

    fun init(context: Context) {
        val date = LocalDate.now()
        currentDay = LoadDay.load(context, date) ?: EatingDay(date)
    }

    fun getCurrentDay() : EatingDay {
        return currentDay
    }
}