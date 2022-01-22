package com.smonhof.foodtracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smonhof.foodtracker.R
import com.smonhof.foodtracker.data.*
import com.smonhof.foodtracker.databinding.FragmentDayoverviewBinding
import com.smonhof.foodtracker.fragments.arguments.IngredientListFragmentArguments
import com.smonhof.foodtracker.fragments.arguments.MealFragmentArguments
import com.smonhof.foodtracker.fragments.recyclerViews.CaloricIntakeRecyclerViewAdapter

class DayOverviewFragment : Fragment() {

    private var _binding: FragmentDayoverviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val currentDay = DataProvider.getCurrentDay()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDayoverviewBinding.inflate(inflater, container, false)

        if(binding.list is RecyclerView) {
            with(binding.list){
                layoutManager = LinearLayoutManager(context)
                adapter = CaloricIntakeRecyclerViewAdapter(currentDay._intake, ::removeMeal, ::editExistingMeal)
            }
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSaveDay.setOnClickListener {
            SaveDay.save(view.context.applicationContext,currentDay)
        }
        binding.buttonAddMeal.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_MealOverview)
        }
        binding.calorieDisplay.updateValue(currentDay.intakeValues)
    }

    private fun removeMeal (id : Int){
        currentDay._intake.removeAt(id)
        val adapter = binding.list.adapter
        if(adapter is CaloricIntakeRecyclerViewAdapter){
            adapter.updateValues(currentDay._intake)
        }
        binding.calorieDisplay.updateValue(currentDay.intakeValues)
    }

    private fun editExistingMeal(id : Int){
        if(id < 0 || id >= currentDay._intake.count()){
            return
        }
        when (val meal : CaloricIntake = currentDay._intake[id]){
            is Meal -> {
                val bundle = bundleOf("ContainerMeal" to MealFragmentArguments(meal, false))
                findNavController().navigate(R.id.action_FirstFragment_to_MealOverview, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}