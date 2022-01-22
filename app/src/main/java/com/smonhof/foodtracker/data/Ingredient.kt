package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Ingredient (val name: String,
                  val ident: String,
                  val values: NutritionalValues,
                  val unit : Unit = Unit.Unknown
): CaloricIntake {
    override val displayName: String
        get() = name
    override val intakeValues: NutritionalValues
        get() = values

    companion object{
        val empty get() = Ingredient("Empty", "invalid", NutritionalValues.empty)

        fun fromSerialized (serialized: SerializedIngredient) =
            Ingredient(
                serialized.resourceId,
                serialized.ident,
                NutritionalValues( serialized.calories.toFloat() / serialized.size.toFloat(),
                    serialized.carbs.toFloat() / serialized.size.toFloat(),
                    serialized.protein.toFloat() / serialized.size.toFloat(),
                    serialized.fat.toFloat() / serialized.size.toFloat()),
                    UnitHelper.getUnit(serialized.unit))
        fun fromJson (json: String) = fromSerialized(Json.decodeFromString<SerializedIngredient>(json))
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