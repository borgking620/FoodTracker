package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

@Serializable
class CustomMeal (val _name : String,
                  val _calories : Float,
                  val _carbs : Float,
                  val _protein : Float,
                  val _fat : Float) : CaloricIntake {
    override val displayName: String
        get() = _name
    override val intakeValues: NutritionalValues
        get() = NutritionalValues(_calories, _carbs, _protein, _fat)
    companion object{
        val empty = CustomMeal("",0f,0f,0f,0f)
    }
}