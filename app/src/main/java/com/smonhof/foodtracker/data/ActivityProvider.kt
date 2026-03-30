package com.smonhof.foodtracker.data

import android.view.Window
import android.view.WindowManager

object ActivityProvider {
    private lateinit var window : Window

    fun init(window: Window){
        this.window = window
    }

    fun setWindowFixed(isFixed : Boolean){
        if(isFixed){
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}