package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

class Ingredient (val resource: Resource,
                  val ident: String,
                  val values: NutritionalValues,
                  val unit : Unit = Unit.Unknown
): CaloricIntake {
    override val displayName: String
        get() = resource._name
    override val intakeValues: NutritionalValues
        get() = values

    companion object{
        val empty get() = Ingredient(Resource("Empty"), "invalid", NutritionalValues.empty)

        fun fromSerialized (serialized: SerializedIngredient, res : Map<String,Resource>) =
            Ingredient(
                res[serialized.resourceId] ?:Resource(serialized.resourceId),
                serialized.ident,
                NutritionalValues( serialized.calories.toFloat() / serialized.size.toFloat(),
                    serialized.carbs.toFloat() / serialized.size.toFloat(),
                    serialized.protein.toFloat() / serialized.size.toFloat(),
                    serialized.fat.toFloat() / serialized.size.toFloat()),
                    UnitHelper.getUnit(serialized.unit))
    }
}

@Serializable
data class SerializedIngredient (val ident: String,
                            val group: String,
                            val resourceId: String,
                            val calories: Int,
                            val carbs: Int,
                            val protein: Int,
                            val fat: Int,
                            val size: Int,
                            val unit: Int)