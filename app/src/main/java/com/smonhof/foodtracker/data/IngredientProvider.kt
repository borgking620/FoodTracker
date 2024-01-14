package com.smonhof.foodtracker.data

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.smonhof.foodtracker.R
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

object IngredientProvider {

    private var group: Group? = null
    private lateinit var ingredientsByIdent : Map<String,Ingredient>
    private lateinit var snacksByIdent : Map<String, IngredientSnack>

    fun init(resources: Resources, context: Context){
        val ingredientsFileContent = getFileContent(resources, R.raw.data_ingredients)
        val groupsFileContent = getFileContent(resources, R.raw.data_groups)
        val ingredientSnackFileContent = getFileContent(resources, R.raw.data_ingredientsnacks)
        val standaloneSnackFileContent = getFileContent(resources, R.raw.data_standalonesnacks)
        val resourcesFileContent = getFileContent(resources, R.raw.data_resources)

        val serializedResources = Json.decodeFromString<SerializedResourcesList>(resourcesFileContent).resources
        val serializedIngredients = Json.decodeFromString<SerializedIngredientList>(ingredientsFileContent).ingredients
        val serializedGroups = Json.decodeFromString<SerializedGroupList>(groupsFileContent).groups
        val serializedIngredientSnacks = Json.decodeFromString<SerializedIngredientSnackList>(ingredientSnackFileContent).ingredientsnacks
        val serializedStandaloneSnacks = Json.decodeFromString<SerializedStandaloneSnackList>(standaloneSnackFileContent).standalonesnacks


        Log.e("", "Decoding Resources")
        val resourcesFromIdent = serializedResources.associate { r -> r.ident to Resource.fromSerialized(r,context) }
        Log.e("", "Decoded Resources")
        val ingredientsToGroups = getMapOf(serializedIngredients,{ing -> ing.group},{ing->Ingredient.fromSerialized(ing,resourcesFromIdent)})
        ingredientsByIdent =
            ingredientsToGroups.values.fold(emptyList<Ingredient>()) { acc, ing -> acc + ing }.associateBy { it.ident }
        // snacks require ingredientsByIdent
        val snacksToGroups = mergeMaps(
            getMapOf(serializedStandaloneSnacks,{ snk -> snk.group},{ snk -> IngredientSnack.fromSerialized(snk, resourcesFromIdent)}),
            getMapOf(serializedIngredientSnacks,{snk -> snk.group},{snk -> IngredientSnack.fromSerialized(snk, resourcesFromIdent)})
        )

        snacksByIdent = snacksToGroups.values.fold(emptyList<IngredientSnack>()){acc, snk -> acc + snk}.associateBy { it._ident }

        val groupsToHierarchy = getMapOf(serializedGroups,{grp -> grp.parent},{grp->grp})
        val convertedGroups = emptyMap<String,Group>().toMutableMap()
        val parentGroups = groupsToHierarchy[""]?.mapNotNull call@{
            val newGroup = getGroup(it, convertedGroups, ingredientsToGroups, groupsToHierarchy, snacksToGroups, resourcesFromIdent)
            if (newGroup != null) return@call Pair(it.ident, newGroup) else return@call null
        }?.toMap()?: emptyMap()

        group = parentGroups["ingredients"]
    }

    private fun getGroup (group: SerializedGroup,
                          convertedGroups: MutableMap<String, Group>,
                          allIngredients : Map<String,List<Ingredient>>,
                          groups: Map<String, List<SerializedGroup>>,
                          snacks: Map<String, List<IngredientSnack>>,
                          res : Map<String, Resource>) : Group?
    {
        if(convertedGroups.containsKey(group.ident)){
            return convertedGroups[group.ident]
        }

        val subgroups =
            (if (groups.contains(group.ident)) groups[group.ident]!! else emptyList())
            .map{
            getGroup(it, convertedGroups, allIngredients, groups, snacks, res)
        }.filterNotNull().toTypedArray()

        val ingredients = allIngredients?.get(group.ident)?.toTypedArray()?: emptyArray()

        val snacks = snacks?.get(group.ident)?.toTypedArray()?: emptyArray()
        val convertedGroup = Group(res[group.resourceId] ?:Resource(group.resourceId), subgroups, ingredients, snacks)
        convertedGroups[group.ident] = convertedGroup
        return convertedGroup
    }

    private fun <T,T2> getMapOf(collection : Collection<T>, getKey: (T) -> String, getValue: (T) -> T2): Map<String,MutableList<T2>>{
        val map : MutableMap<String, MutableList<T2>> = emptyMap<String,MutableList<T2>>().toMutableMap()
        collection.forEach{obj ->
            val key = getKey(obj)
            val value = getValue(obj)
            if(!map.containsKey(key)){
                map[key] = mutableListOf(value)
            }
            else{
                map[key]?.add(value)
            }
        }
        return map.toMap()
    }

    private fun <T> mergeMaps(a:Map<String, MutableList<T>>, b:Map<String, MutableList<T>>) : Map<String, MutableList<T>>{
        val newList = a.toMutableMap()
        b.forEach{
            if(newList.containsKey(it.key)){
                newList[it.key]?.addAll(it.value)
            }
            else{
                newList[it.key] = it.value
            }
        }
        return newList.toMap()
    }

    private fun getFileContent(source:Resources, file : Int): String{
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
            ""
        }

    }

    fun getIngredients(): Group {
        if(group == null){
            return Group(Resource(""), emptyArray(), emptyArray(), emptyArray())
        }
        return group!!
    }

    fun findIngredient(ident : String) : Ingredient?{
        return ingredientsByIdent[ident]
    }

    fun findSnack (ident : String) : IngredientSnack? {
        return snacksByIdent[ident]
    }

    @Serializable
    data class SerializedIngredientList(val ingredients :List<SerializedIngredient> = emptyList())

    @Serializable
    data class SerializedGroupList(val groups : List<SerializedGroup> = emptyList())

    @Serializable
    data class SerializedIngredientSnackList(val ingredientsnacks: List<SerializedIngredientSnack> = emptyList())

    @Serializable
    data class SerializedStandaloneSnackList(val standalonesnacks: List<SerializedStandaloneSnack> = emptyList())

    @Serializable
    data class SerializedResourcesList(val resources: List<SerializedResource> = emptyList())
}