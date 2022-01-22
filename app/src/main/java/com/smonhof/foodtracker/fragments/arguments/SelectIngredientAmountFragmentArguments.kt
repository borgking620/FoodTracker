package com.smonhof.foodtracker.fragments.arguments

import com.smonhof.foodtracker.data.*
import java.io.Serializable

class SelectIngredientAmountFragmentArguments (val _ingredient : Ingredient,
                                               val _onSelected : (IngredientAmount)-> Unit = { _ -> }) : Serializable