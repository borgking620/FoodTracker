package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

class Group (val resource : Resource,
             val subGroups : Array<Group>,
             val ingredients :Array<Ingredient>,
             val snacks : Array<IngredientSnack>){

    fun isEmpty(ignoreIngredients: Boolean, ignoreSnacks : Boolean) : Boolean =
                (ignoreIngredients || ingredients.size == 0) &&
                (ignoreSnacks || snacks.size == 0) &&
                subGroups.firstOrNull { grp -> !grp.isEmpty(ignoreIngredients, ignoreSnacks) } == null

    fun listContent(insetCount : Int) : String{
        val inset = "| ".repeat(insetCount)
        val stringbuilder = StringBuilder()
        stringbuilder.append(inset)
        stringbuilder.append("[G]" + resource._name + "\n")
        for(gr in subGroups){
            stringbuilder.append(gr.listContent(insetCount + 1))
        }
        for(ing in ingredients){
            stringbuilder.append(inset)
            stringbuilder.append("|- [I]" + ing.resource._name + "\n")
        }
        for(snk in snacks){
            stringbuilder.append(inset)
            stringbuilder.append("|- [S]" + snk._resource._name + "\n")
        }
        return stringbuilder.toString()
    }

    companion object{
        val emtpy = Group(Resource(""), emptyArray(), emptyArray(), emptyArray())
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