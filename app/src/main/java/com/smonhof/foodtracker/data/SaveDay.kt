package com.smonhof.foodtracker.data

import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Exception

object SaveDay {

    fun save(context: Context, day: EatingDay){
        saveFile(context, getContentFileName(day), getContentFileContent(day))
        saveFile(context, getThumbFileName(day), getThumbFileContent(day))
    }

    private fun saveFile (context : Context, name : String, content : String){
        Log.d(null, "Saving: $content to $name")
        try {
            context.openFileOutput(name, Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
            }
        }
        catch (e:Exception){
            Log.e(null, e.toString())
        }
    }

    private fun getContentFileName(day: EatingDay) : String
        = "dayContent_${day.date.year}_${day.date.monthValue}_${day.date.dayOfMonth}.json"
    private fun getContentFileContent(day:EatingDay) : String
        = Json.encodeToString(day.asSerialized)
    private fun getThumbFileName(day: EatingDay) : String
            = "dayThumb_${day.date.year}_${day.date.monthValue}_${day.date.dayOfMonth}.json"
    private fun getThumbFileContent(day:EatingDay) : String
            = Json.encodeToString(day.asThumb)
}