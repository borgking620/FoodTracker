package com.smonhof.foodtracker.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import com.smonhof.foodtracker.R
import kotlinx.serialization.Serializable
import java.io.File
import java.io.FileInputStream

class Resource (val _name: String,
                var _icon : Bitmap? = null) {


    companion object{
        fun fromSerialized (serialized:SerializedResource, context : Context) = Resource(
            getLoca("${serialized.locaId}_name",context),
            loadBitmap(serialized.icon,context))

        fun getLoca(locaId : String, context : Context) : String = try {
            context.resources.getString(
                context.resources.getIdentifier(locaId,"string", context.packageName))
        } catch (e:Exception){
            "$locaId (Not found)"}

        fun loadBitmap (fileName : String, context: Context) : Bitmap? {
            try {
                val resourceId = context.resources.getIdentifier(fileName, "drawable", context.packageName)
                val bitmap = BitmapFactory.decodeResource(context.resources,resourceId)
                return bitmap;
            } catch (e : Exception){
                Log.e(null,e.toString())
                return null
            }
            finally{

            }
        }
    }
}

@Serializable
data class SerializedResource(val ident : String, val icon: String, val locaId : String)