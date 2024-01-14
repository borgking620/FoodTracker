package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

abstract class Snack : CaloricIntake {
    abstract val asSerialized : SerializedSnack

    companion object {
        val empty = InvalidSnack("invalid")
    }
}

class ValidSnack(val _ingredient : IngredientSnack) : Snack () {
    override val displayName: String
        get() = _ingredient.displayName
    override val intakeValues: NutritionalValues
        get() = _ingredient.intakeValues
    override val asSerialized: SerializedSnack
        get() = SerializedSnack(_ingredient._ident)
}

class InvalidSnack(private val _ident : String) : Snack() {
    override val displayName: String
        get() = "!INVALID! $_ident"
    override val intakeValues: NutritionalValues
        get() = NutritionalValues.empty
    override val asSerialized: SerializedSnack
        get() = SerializedSnack(_ident)
}

@Serializable
class SerializedSnack(private val _ident :String){
    fun deserialize () : Snack {
        val snack = IngredientProvider.findSnack(_ident)
        return if(snack == null) {
            InvalidSnack(_ident)
        }
        else {
            ValidSnack(snack)
        }

    }

}