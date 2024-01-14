package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

class IngredientSnack (val _resource : Resource,
                       val _ident : String,
                       val _values : NutritionalValues) : CaloricIntake {
    override val displayName: String
        get() = _resource._name
    override val intakeValues: NutritionalValues
        get() = _values

    companion object{
        val empty = IngredientSnack(Resource("Empty"),"invalid", NutritionalValues.empty)

        fun fromSerialized(serialized : SerializedIngredientSnack, res : Map<String, Resource>) =
            IngredientSnack(
                res.get(serialized.resourceId)?:Resource(serialized.resourceId),
                serialized.ident,
                IngredientProvider.findIngredient(serialized.ingredient)?.intakeValues?.times(serialized.amount)?: NutritionalValues.empty)

        fun fromSerialized(serialized : SerializedStandaloneSnack, res : Map<String, Resource>) =
            IngredientSnack(res.get(serialized.resourceId)?:Resource(serialized.resourceId),
                serialized.ident,
                NutritionalValues(
                    serialized.calories,
                    serialized.carbs,
                    serialized.protein,
                    serialized.fat
                ))
    }
}

@Serializable
data class SerializedIngredientSnack(val ident : String,
                                     val group : String,
                                     val resourceId : String,
                                     val ingredient : String,
                                     val amount: Float)

@Serializable
data class SerializedStandaloneSnack(val ident : String,
                                     val group : String,
                                     val resourceId : String,
                                     val calories: Float,
                                     val carbs: Float,
                                     val protein: Float,
                                     val fat: Float)
