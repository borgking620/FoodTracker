package com.smonhof.foodtracker.data

import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Exception

object SaveDay {

    fun save(context: Context, day: EatingDay){
        val fileName = getContentFileName(day)
        val fileContent = getContentFileContent(day)
        Log.d(null, "Saving: $fileContent to $fileName")
        try {
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(fileContent.toByteArray())
            }
        }
        catch (e:Exception){
            Log.e(null, e.toString())
        }
    }

    // add Thumb later
    private fun getContentFileName(day: EatingDay) : String
        = "dayContent_${day.date.year}_${day.date.monthValue}_${day.date.dayOfMonth}.json"
    private fun getContentFileContent(day:EatingDay) : String
        = Json.encodeToString(day.asSerialized)
}