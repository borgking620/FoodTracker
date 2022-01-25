package com.smonhof.foodtracker.data

class IngredientSnack (val _ident : String,
                       val _values : NutritionalValues) : CaloricIntake {
    override val displayName: String
        get() = _ident
    override val intakeValues: NutritionalValues
        get() = _values
}