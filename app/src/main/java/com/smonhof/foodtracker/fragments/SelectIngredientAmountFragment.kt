package com.smonhof.foodtracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.smonhof.foodtracker.data.*
import com.smonhof.foodtracker.databinding.FragmentSelectingredientamountBinding
import com.smonhof.foodtracker.fragments.arguments.SelectIngredientAmountFragmentArguments

class SelectIngredientAmountFragment : Fragment() {
    private var _binding : FragmentSelectingredientamountBinding?= null
    private val binding get() = _binding!!
    private var _ingredient = Ingredient.empty
    private var _amount = 1f
    private var _onSave : (IngredientAmount) -> Unit = { _ ->}

    override fun onCreateView(inflater: LayoutInflater,
                              container:ViewGroup?,
                              savedInstanceState: Bundle?) : View?{
        _binding = FragmentSelectingredientamountBinding.inflate(inflater, container, false)
        fetchArguments()
        UpdateAmountDisplay()
        binding.ingredientName.text = _ingredient.displayName
        binding.confirmButton.setOnClickListener(::onSaveClicked)
        binding.selectSmallPortion.setOnClickListener (onPreSelectAmount(0.5f))
        binding.selectMediumPortion.setOnClickListener(onPreSelectAmount(1f))
        binding.selectLargePortion.setOnClickListener (onPreSelectAmount(2f))

        binding.ingredientAmount.doOnTextChanged { text, _, _, _ ->
            val newAmount = text.toString().toFloatOrNull()
            if(newAmount != null){
                _amount = newAmount
            }
        }

        return binding.root
    }

    private fun UpdateAmountDisplay(){
        binding.ingredientAmount.setText(_amount.toString())
    }

    private fun fetchArguments() {
        val container = arguments?.get("ContainerIngredient")
        if(container is SelectIngredientAmountFragmentArguments){
            _ingredient = container._ingredient
            _onSave = container._onSelected
        }
    }

    private fun onPreSelectAmount(amount : Float) : (View) -> Unit {
        return {
            _amount = amount
            UpdateAmountDisplay()
        }
    }

    private fun onSaveClicked(view: View?) {
        _onSave(ValidIngredientAmount(_ingredient,_amount))
    }
}