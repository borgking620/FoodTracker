package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable
import java.time.LocalDate

class EatingDay (val date : LocalDate = LocalDate.now()) : CaloricIntake {
    val _intake = MutableList<Meal>(0) { Meal("") }

    override val displayName: String
        get() = date.toString()
    override val intakeValues : NutritionalValues
        get () = _intake.fold(NutritionalValues.empty){ acc, new -> acc + new.intakeValues}
    val asSerialized : SerializedEatingDay
        get() = SerializedEatingDay(date.year,date.monthValue,date.dayOfMonth,_intake.map{it.asSerialized})
}

@Serializable
class SerializedEatingDay(val year : Int,
                          val month: Int,
                          val day : Int,
                          val meals: List<SerializedMeal>){
    val deSerialize : EatingDay get() {
        val day = EatingDay(LocalDate.of(year, month, day))
        meals.forEach {day._intake.add(it.deserialize)}
        return day
    }
}