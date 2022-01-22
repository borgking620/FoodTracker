package com.smonhof.foodtracker.data

class Snack (private val name : String,
             private val value: ValidIngredientAmount) : CaloricIntake {
    override val displayName: String
        get() = name
    override val intakeValues: NutritionalValues
        get() = value.intakeValues
}