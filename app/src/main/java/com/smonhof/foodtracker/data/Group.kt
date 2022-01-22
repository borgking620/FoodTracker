package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

class Group (val name : String,
             val subGroups : Array<Group>,
             val ingredients :Array<Ingredient>){
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