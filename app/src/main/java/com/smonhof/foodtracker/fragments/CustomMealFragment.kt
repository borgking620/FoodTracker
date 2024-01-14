package com.smonhof.foodtracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smonhof.foodtracker.data.CustomMeal
import com.smonhof.foodtracker.databinding.FragmentCustommealBinding
import com.smonhof.foodtracker.fragments.arguments.CustomMealFragmentArguments

class CustomMealFragment : Fragment() {

    private var _binding: FragmentCustommealBinding? = null
    private var _onFinished : (CustomMeal) -> Unit = {}

    private var _name : String = ""
    private var _calories : Float = 0f
    private var _carbs : Float = 0f
    private var _protein : Float = 0f
    private var _fat : Float = 0f
    private var _portions : Float = 1f

    private val binding get() = _binding!!

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        _binding = FragmentCustommealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArguments()

        binding.buttonSave.setOnClickListener {
            _onFinished(CustomMeal(_name,_calories,_carbs,_protein,_fat, _portions))
            findNavController().popBackStack()
        }
        binding.mealName.doOnTextChanged { text, _, _, _ ->
            _name = text.toString()
        }
        binding.mealCalories.doOnTextChanged (onTextChanged {i->_calories = i})
        binding.mealCarbs.doOnTextChanged (onTextChanged{i-> _carbs = i})
        binding.mealProtein.doOnTextChanged (onTextChanged{i-> _protein = i})
        binding.mealFat.doOnTextChanged (onTextChanged {i-> _fat = i})
        binding.mealPortions.doOnTextChanged (onTextChanged {i-> _portions = i})
    }

    private fun onTextChanged(action: (Float) -> Unit) : (CharSequence?, Int,Int,Int) -> Unit{
        return { text ,_,_,_ ->
            val newAmount = text.toString().toFloatOrNull()
            if(newAmount != null){
                action(newAmount)
            }
        }
    }

    private fun fetchArguments(){
        val args = arguments?.get("ContainerCustomMeal")
        if(args is CustomMealFragmentArguments){
            val previousMeal = args._meal
            if(previousMeal != null){
                _name = previousMeal._name
                _calories = previousMeal._calories
                _carbs = previousMeal._carbs
                _protein = previousMeal._protein
                _fat = previousMeal._fat
                _portions = previousMeal._portions
                binding.mealName.setText(_name)
                binding.mealCalories.setText(_calories.toString())
                binding.mealCarbs.setText(_carbs.toString())
                binding.mealProtein.setText(_protein.toString())
                binding.mealFat.setText(_fat.toString())
                binding.mealPortions.setText(_portions.toString())

            }
            _onFinished = args._onFinished
        }
        else{
            Log.e(null,"Cannot fetch arguments for CustomMealFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}