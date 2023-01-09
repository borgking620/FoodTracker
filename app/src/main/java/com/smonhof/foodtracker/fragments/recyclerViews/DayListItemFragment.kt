package com.smonhof.foodtracker.fragments.recyclerViews

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smonhof.foodtracker.data.ThumbEatingDay
import com.smonhof.foodtracker.databinding.FragmentDayListitemBinding
import java.time.LocalDate

class DayListItemRecyclerViewAdapter(
    private var values: List<ThumbEatingDay>,
    private val onViewClicked: (Int) -> Unit = {_ ->}
) : RecyclerView.Adapter<DayListItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentDayListitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    fun updateValues (newValues : List<ThumbEatingDay>){
        values = newValues
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.dateView.text = displayDate(item)
        holder.calorieView.text = item.nutrition.Calories.toString()
        holder.viewButton.setOnClickListener{onViewClicked(position)}
    }

    override fun getItemCount(): Int =values.size

    private fun displayDate(day : ThumbEatingDay) : String {
        val date = LocalDate.of(day.year,day.month,day.day)
        return "${date.dayOfWeek.name} the ${attachNumberExtension(day.day)}"
    }

    private fun attachNumberExtension(number:Int) : String
    {
        val rest = number % 10
        if(rest == 1) {return "${number}st"}
        if(rest == 2) {return "${number}nd"}
        if(rest == 3) {return "${number}rd"}
        return "${number}th"
    }

    inner class ViewHolder(binding: FragmentDayListitemBinding) : RecyclerView.ViewHolder(binding.root) {
        val dateView: TextView = binding.date
        val calorieView: TextView = binding.calorieCount
        val viewButton: Button = binding.editButton;
    }
}