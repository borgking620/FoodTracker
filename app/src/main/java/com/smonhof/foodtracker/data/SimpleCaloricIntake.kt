package com.smonhof.foodtracker.data

class SimpleCaloricIntake (val name: String,
                           private val values : NutritionalValues
) : CaloricIntake {
    override val displayName: String
        get() = name
    override val intakeValues: NutritionalValues
        get() = values
}