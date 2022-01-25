package com.smonhof.foodtracker.fragments.arguments

import com.smonhof.foodtracker.data.Group
import com.smonhof.foodtracker.data.IngredientAmount
import java.io.Serializable

class IngredientListFragmentArguments (val _group : Group,
                                       val _onIngredientAmountSelected : (IngredientAmount) -> Unit = {}) : Serializable