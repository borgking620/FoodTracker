package com.smonhof.foodtracker.data

import android.content.Context
import android.util.Log
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.nio.CharBuffer
import java.time.LocalDate
import java.util.*
import kotlin.Exception
import kotlin.collections.HashSet

object LoadDay {
    fun load (context: Context, day : LocalDate ) : EatingDay?{
        try {
            val fileName = getContentFileName(day)
            val file = context.openFileInput(fileName)
            val channel = file.channel
            val size = channel.size()
            if(size>Int.MAX_VALUE.toLong()){
                throw IOException("Error Loading file: File is too big!")
            }
            val buffer = CharBuffer.allocate(size.toInt())
            file.bufferedReader().read(buffer)
            buffer.flip()
            val content = buffer.toString()

            channel.close()
            file.close()

            return Json.decodeFromString<SerializedEatingDay>(content).deSerialize
        }
        catch (e:Exception){
            Log.e(null, e.toString())
        }
        return null
    }

    fun loadThumb(context: Context, day : LocalDate) : ThumbEatingDay?
    {
        try{
            val fileName = getThumbFileName(day)
            val thumbExists = context.fileList().contains(fileName)
            val file = if (thumbExists) context.openFileInput(fileName) else context.openFileInput(getContentFileName(day))
            val channel = file.channel
            val size = channel.size()
            if(size>Int.MAX_VALUE.toLong()){
                throw IOException("Error Loading file: File is too big!")
            }
            val buffer = CharBuffer.allocate(size.toInt())
            file.bufferedReader().read(buffer)
            buffer.flip()
            val content = buffer.toString()
            channel.close()
            file.close()

            return if(thumbExists) Json.decodeFromString<ThumbEatingDay>(content) else Json.decodeFromString<SerializedEatingDay>(content).deSerialize.asThumb
        }
        catch (e:Exception) {
            Log.e(null, e.toString())
        }
        return null;
    }

    fun getAllSavedDays(context: Context) : Set<LocalDate>
    {
        val dates = HashSet<LocalDate>().toMutableSet()
        val fileList = context.fileList()
        for(file in fileList) {
            val fileDateOption = getDateFromName(file)
            if(!fileDateOption.isPresent){
                continue
            }
            val fileDate = fileDateOption.get()
            if(dates.contains(fileDate)){
                continue
            }
            dates.add(fileDate)
        }

        return dates;
    }

    private fun getDateFromName(fileName: String) : Optional<LocalDate>
    {
        val file = Optional.of(fileName);
        return file.map{
                f -> f.split('.')}.flatMap{
                f -> if (f.size != 2 || f[1] != "json") Optional.empty() else Optional.of(f[0])}.map {
                f -> f.split('_')}.flatMap{
                f -> if(f.size != 4 || (f[0]!= "dayContent" && f[0] !="dayThumb")) Optional.empty() else Optional.of(f)}.map{
                f -> f.subList(1,4).map {i-> i.toIntOrNull()}}.flatMap {
                f -> if(f[0] == null || f[1] == null || f[2] == null) Optional.empty() else Optional.of(LocalDate.of(f[0]!!, f[1]!!, f[2]!!)) }
    }

    fun getContentFileName(day: LocalDate) : String
            = "dayContent_${day.year}_${day.monthValue}_${day.dayOfMonth}.json"
    fun getThumbFileName(day: LocalDate) : String
            = "dayThumb_${day.year}_${day.monthValue}_${day.dayOfMonth}.json"
}