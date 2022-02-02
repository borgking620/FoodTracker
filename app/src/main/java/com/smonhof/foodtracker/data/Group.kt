package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

class Group (val name : String,
             val subGroups : Array<Group>,
             val ingredients :Array<Ingredient>,
             val snacks : Array<IngredientSnack>){

    fun isEmpty(ignoreIngredients: Boolean, ignoreSnacks : Boolean) : Boolean =
                (ignoreIngredients || ingredients.size == 0) &&
                (ignoreSnacks || snacks.size == 0) &&
                subGroups.firstOrNull { grp -> !grp.isEmpty(ignoreIngredients, ignoreSnacks) } == null

    companion object{
        val emtpy = Group("", emptyArray(), emptyArray(), emptyArray())
    }
}

@Serializable
class SerializedGroup(
    val ident: String,
    val parent: String,
    val resourceId: String
){
    override fun toString(): String {
        return "Serialized Object: " + ident + " under " + parent + " with " + resourceId
    }
}