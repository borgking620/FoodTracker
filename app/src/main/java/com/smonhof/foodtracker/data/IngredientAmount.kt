package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

abstract class IngredientAmount : CaloricIntake {
    abstract val asSerialized : SerializedIngredientAmount
}

class ValidIngredientAmount (val ingredient: Ingredient,
                             val amount: Float) : IngredientAmount() {
    override val displayName: String
        get() = ingredient.displayName
    override val intakeValues: NutritionalValues
        get() = ingredient.intakeValues * amount
    override val asSerialized get() = SerializedIngredientAmount(ingredient.ident, amount)
}

class InvalidIngredientAmount (val ident: String, val amount: Float) : IngredientAmount()
{
    override val displayName: String
        get() = "!INVALID! $ident"
    override val intakeValues: NutritionalValues
        get() = NutritionalValues.empty
    override val asSerialized: SerializedIngredientAmount
        get() = SerializedIngredientAmount(ident, amount)
}


@Serializable
class SerializedIngredientAmount(val ingredientIdent :String,
                                 val amount :Float) {
    fun deserialize () :IngredientAmount
    {
        val ingredient = IngredientProvider.findIngredient(ingredientIdent)
        return if(ingredient == null){
            InvalidIngredientAmount(ingredientIdent, amount)
        } else{
            ValidIngredientAmount(ingredient, amount)
        }
    }
}