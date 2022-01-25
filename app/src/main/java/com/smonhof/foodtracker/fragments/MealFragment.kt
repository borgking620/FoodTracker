package com.smonhof.foodtracker.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.smonhof.foodtracker.R
import com.smonhof.foodtracker.data.*
import com.smonhof.foodtracker.databinding.FragmentMealBinding
import com.smonhof.foodtracker.fragments.recyclerViews.IngredientAmountRecyclerViewAdapter
import com.smonhof.foodtracker.fragments.arguments.IngredientListFragmentArguments
import com.smonhof.foodtracker.fragments.arguments.MealFragmentArguments

/**
 * A fragment representing a list of Items.
 */
class MealFragment : Fragment() {

    private var _binding : FragmentMealBinding? = null
    private val binding get() = _binding!!

    private lateinit var meal : Meal
    private var addMealToDay : Boolean = false

    private lateinit var mealNameTextWatcher : TextWatcher

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMealBinding.inflate(inflater, container, false)

        val mealArguments = arguments?.get("ContainerMeal") as? MealFragmentArguments

        if (mealArguments == null){
            meal = Meal("")
            addMealToDay = true
        }
        else {
            meal = mealArguments.meal
            addMealToDay = mealArguments.newMeal
        }

        mealNameTextWatcher = MealNameChangedTextWatcher(meal)
        if (binding.list is RecyclerView) {
            with(binding.list) {
                layoutManager = LinearLayoutManager(context)
                adapter = IngredientAmountRecyclerViewAdapter(meal.ingredientList, :: removeIngredient)
            }
        }
        binding.addIngredientButton.setOnClickListener{
            val args = IngredientListFragmentArguments(IngredientProvider.getIngredients(), ::ingredientAmountSelected)
            findNavController().navigate(R.id.action_MealContent_to_IngredientList, bundleOf("ContainerGroup" to args))
        }
        binding.saveButton.setOnClickListener{
            if(addMealToDay){
                DataProvider.getCurrentDay()._meals.add(meal)
            }
            findNavController().popBackStack()
        }
        binding.mealName.setText(meal.name)
        binding.mealName.addTextChangedListener(mealNameTextWatcher)

        binding.caloriesDisplay.updateValue(meal.intakeValues)

        return binding.root
    }

    private fun removeIngredient(index : Int){
        meal.removeIngredient(index)
        val adapter = binding.list.adapter
        if(adapter is IngredientAmountRecyclerViewAdapter){
            adapter.updateValues(meal.ingredientList)
        }
        binding.caloriesDisplay.updateValue(meal.intakeValues)
    }

    private class MealNameChangedTextWatcher (val meal : Meal) : TextWatcher {
        override fun afterTextChanged(editable: Editable) {}
        override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(newText: CharSequence, start: Int, before: Int, count: Int) {
            meal.name= newText.toString()
        }
    }

    private fun ingredientAmountSelected (ingredientAmount: IngredientAmount) {
        meal.addIngredient(ingredientAmount)
        val navController = findNavController()
        navController.popBackStack(R.id.MealContentList,true)
        val bundle = bundleOf("ContainerMeal" to MealFragmentArguments(meal, addMealToDay))
        navController.navigate(R.id.action_FirstFragment_to_MealOverview, bundle)
    }
}