package com.smonhof.foodtracker.fragments.arguments

import com.smonhof.foodtracker.data.CustomMeal
import java.io.Serializable

class CustomMealFragmentArguments (
    val _meal : CustomMeal?,
    val _onFinished : (CustomMeal) -> Unit = {}) : Serializable