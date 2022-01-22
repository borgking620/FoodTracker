package com.smonhof.foodtracker.fragments.arguments

import com.smonhof.foodtracker.data.Group
import com.smonhof.foodtracker.data.IngredientAmount
import java.io.Serializable

class IngredientListFragmentArguments (val group : Group,
                                       val onIngredientAmountSelected : (IngredientAmount) -> Unit = {}) : Serializable