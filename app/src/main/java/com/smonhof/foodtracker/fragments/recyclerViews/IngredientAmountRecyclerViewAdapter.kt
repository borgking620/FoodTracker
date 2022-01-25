package com.smonhof.foodtracker.fragments.recyclerViews

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.smonhof.foodtracker.data.IngredientAmount
import com.smonhof.foodtracker.data.InvalidIngredientAmount
import com.smonhof.foodtracker.data.ValidIngredientAmount
import com.smonhof.foodtracker.data.UnitHelper
import com.smonhof.foodtracker.databinding.FragmentIngredientamountBinding

class IngredientAmountRecyclerViewAdapter(
    private var values: List<IngredientAmount>,
    private val onRemoveClicked : ((Int)->Unit)? = null
) : RecyclerView.Adapter<IngredientAmountRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(FragmentIngredientamountBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    public fun updateValues (newValues : List<IngredientAmount>){
        values = newValues
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        when (item){
            is ValidIngredientAmount -> {
                holder.nameView.text = item.displayName
                holder.amountView.text = UnitHelper.displayUnit(item.unit, item.amount)
                holder.calorieView.text = UnitHelper.displayUnit(com.smonhof.foodtracker.data.Unit.Calorie,item.intakeValues.Calories)
            }
            is InvalidIngredientAmount -> {
                holder.nameView.text = item.displayName
                holder.amountView.text = item.amount.toString()
                holder.calorieView.text = "???"
            }
            else -> {
                holder.nameView.text = item.displayName
                holder.amountView.text = "???"
                holder.calorieView.text = "Unknown Type"
            }
        }
        if(onRemoveClicked != null){
            holder.removeButton.setOnClickListener{ onRemoveClicked?.let { it1 -> it1(position) } }
        }
        else{
            holder.removeButton.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentIngredientamountBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.ingredientName
        val amountView: TextView = binding.ingredientAmount
        val calorieView: TextView = binding.ingredientCalories
        val removeButton: Button = binding.removeButton
    }

}