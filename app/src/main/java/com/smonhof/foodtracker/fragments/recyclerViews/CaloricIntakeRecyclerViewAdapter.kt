package com.smonhof.foodtracker.fragments.recyclerViews

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.smonhof.foodtracker.data.CaloricIntake
import com.smonhof.foodtracker.data.UnitHelper
import com.smonhof.foodtracker.databinding.FragmentMealListitemBinding

class CaloricIntakeRecyclerViewAdapter(
    private var values: List<CaloricIntake>,
    private val onRemoveClicked : (Int) -> Unit = {_ ->},
    private val onEditClicked : (Int) -> Unit = {_ ->}
) : RecyclerView.Adapter<CaloricIntakeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(FragmentMealListitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun updateValues (newValues : List<CaloricIntake>) {
        values = newValues
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nameView.text = item.displayName
        holder.calorieView.text = UnitHelper.displayUnit(com.smonhof.foodtracker.data.Unit.Calorie,item.intakeValues.Calories)
        holder.removeButton.setOnClickListener{onRemoveClicked(position)}
        holder.editButton.setOnClickListener{onEditClicked(position)}
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentMealListitemBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.mealName
        val calorieView: TextView = binding.calorieCount
        val removeButton : Button = binding.removeButton
        val editButton : Button = binding.editButton
    }
}