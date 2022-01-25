package com.smonhof.foodtracker.data

import android.content.res.Resources
import android.renderscript.ScriptGroup
import android.util.Log
import com.smonhof.foodtracker.R
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.util.*

object IngredientProvider {

    private var group: Group? = null
    private var ingredientsByName : MutableMap<String,Ingredient> = emptyMap<String,Ingredient>().toMutableMap()

    fun init(resources: Resources){
        val ingredientsFileContent = getFileContent(resources, R.raw.data_ingredients)
        val groupsFileContent = getFileContent(resources, R.raw.data_groups)

        if(ingredientsFileContent == null || groupsFileContent == null){
            Log.e(null, "Error opening Ingredients!")
            return
        }

        val serializedIngredients = Json.decodeFromString<SerializedIngredientList>(ingredientsFileContent).ingredients
        val serializedGroups = Json.decodeFromString<SerializedGroupList>(groupsFileContent).groups

        val ingredientsToGroups = getMapOf(serializedIngredients){ing -> ing.group}
        val groupsToHierarchy = getMapOf(serializedGroups){grp -> grp.parent}

        val convertedGroups = emptyMap<String,Group>().toMutableMap()

        val parentGroups = groupsToHierarchy[""]?.mapNotNull call@{
            val newGroup = getGroup(it, convertedGroups, ingredientsToGroups, groupsToHierarchy)
            if (newGroup != null) return@call Pair(it.ident, newGroup) else return@call null
        }?.toMap()?: emptyMap()

        group = parentGroups["ingredients"]
    }

    private fun getGroup (group: SerializedGroup,
                          convertedGroups: MutableMap<String, Group>,
                          allIngredients : Map<String,List<SerializedIngredient>>,
                          groups: Map<String, List<SerializedGroup>>) : Group?
    {
        if(convertedGroups.containsKey(group.ident)){
            return convertedGroups[group.ident]
        }

        val subgroups =
            (if (groups.contains(group.ident)) groups[group.ident]!! else emptyList())
            .map{
            getGroup(it, convertedGroups, allIngredients, groups)
        }.filterNotNull().toTypedArray()

        val ingredients = allIngredients?.get(group.ident)?.map{Ingredient.fromSerialized(it)}?.toTypedArray()?: emptyArray()
        ingredients.forEach { ingredientsByName[it.ident] = it }

        val convertedGroup = Group(group.resourceId, subgroups, ingredients)
        convertedGroups[group.ident] = convertedGroup
        return convertedGroup
    }

    private fun <T> getMapOf(collection : Collection<T>, getKey: (T) -> String): Map<String,MutableList<T>>{
        val map : MutableMap<String, MutableList<T>> = emptyMap<String,MutableList<T>>().toMutableMap()
        collection.forEach{obj ->
            val key = getKey(obj)
            if(!map.containsKey(key)){
                map[key] = mutableListOf(obj)
            }
            else{
                map[key]?.add(obj)
            }
        }
        return map.toMap()
    }

    private fun getFileContent(source:Resources, file : Int): String?{
        return try{
            val stream = source.openRawResource(file)
            val isr = InputStreamReader(stream)
            val reader = BufferedReader(isr)
            val text = reader.readText()
            reader.close()
            isr.close()
            stream.close()
            text
        } catch(e:Exception){
            Log.e(null, e.toString())
            null
        }

    }

    fun getIngredients(): Group {
        if(group == null){
            return Group("", emptyArray(), emptyArray())
        }
        return group!!
    }

    fun findIngredient(ident : String) : Ingredient?{
        return ingredientsByName[ident]
    }

    fun findSnack (ident : String) : IngredientSnack? {
        return null
    }

    @Serializable
    data class SerializedIngredientList(val ingredients :List<SerializedIngredient>)

    @Serializable
    data class SerializedGroupList(val groups : List<SerializedGroup>)
}