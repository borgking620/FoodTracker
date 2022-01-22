package com.smonhof.foodtracker.data

import android.content.Context
import android.util.Log
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.time.LocalDate

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

            Log.d(null, "Available files: ${context.fileList().fold(""){a,b -> a + b}}")
            Log.d(null,"Loading ${content} from ${fileName} with size ${size}" )

            channel.close()
            file.close()

            return Json.decodeFromString<SerializedEatingDay>(content).deSerialize
        }
        catch (e:Exception){
            Log.e(null, e.toString())
        }
        return null
    }

    fun getContentFileName(day: LocalDate) : String
            = "dayContent_${day.year}_${day.monthValue}_${day.dayOfMonth}.json"
}