package com.smonhof.foodtracker.data

import android.util.Log
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

class InvalidIngredientAmount (private val _ident: String,
                               private val _amount: Float) : IngredientAmount() {
    override val displayName: String
        get() = "!INVALID! $_ident"
    override val intakeValues: NutritionalValues
        get() = NutritionalValues.empty
    override val asSerialized: SerializedIngredientAmount
        get() = SerializedIngredientAmount(_ident, _amount)

    val amount = _amount
}


@Serializable
class SerializedIngredientAmount(val ingredientIdent :String,
                                 val amount :Float) {
    fun deserialize (): IngredientAmount {
        val ingredient = IngredientProvider.findIngredient(ingredientIdent)
        return if(ingredient == null){
            Log.e(null, "Cannot find Ingredient: $ingredientIdent")
            InvalidIngredientAmount(ingredientIdent, amount)
        } else{
            ValidIngredientAmount(ingredient, amount)
        }
    }
}