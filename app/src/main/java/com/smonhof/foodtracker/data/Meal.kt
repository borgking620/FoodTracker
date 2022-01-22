package com.smonhof.foodtracker.data

import kotlinx.serialization.Serializable

class Meal (var name : String) : CaloricIntake {
    private val ingredients = MutableList<IngredientAmount>(0) { ValidIngredientAmount(Ingredient.empty, 0f) }

    val ingredientList get() : List<IngredientAmount> = ingredients
    override val displayName: String
        get() = name
    override val intakeValues: NutritionalValues
        get() = ingredients.fold(NutritionalValues.empty){a, b -> a + b.intakeValues}

    fun removeIngredient(id : Int){
        ingredients.removeAt(id)
    }
    fun addIngredient(ingredient: IngredientAmount){
        ingredients.add(ingredient)
    }
    val asSerialized get() = SerializedMeal(name, ingredientList.map {ia -> ia.asSerialized})
}

@Serializable
class SerializedMeal(val name: String,
                     val ingredients : List<SerializedIngredientAmount>) {
    val deserialize : Meal get() {
        val meal = Meal(name)
        ingredients.forEach { meal.addIngredient(it.deserialize())}
        return meal
    }
}