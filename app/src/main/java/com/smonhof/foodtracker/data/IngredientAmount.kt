package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

abstract class IngredientAmount : CaloricIntake {
    abstract val asSerialized : SerializedIngredientAmount
}

class ValidIngredientAmount (private val _ingredient: Ingredient,
                             private val _amount: Float) : IngredientAmount() {
    override val displayName: String
        get() = _ingredient.displayName
    override val intakeValues: NutritionalValues
        get() = _ingredient.intakeValues * _amount
    override val asSerialized get() = SerializedIngredientAmount(_ingredient.ident, _amount)
    val unit = _ingredient.unit
    val amount = _amount
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