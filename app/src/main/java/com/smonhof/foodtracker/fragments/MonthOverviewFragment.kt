package com.smonhof.foodtracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smonhof.foodtracker.data.DataProvider
import com.smonhof.foodtracker.data.EatingDay
import com.smonhof.foodtracker.data.LoadDay
import com.smonhof.foodtracker.data.ThumbEatingDay
import com.smonhof.foodtracker.databinding.FragmentMonthoverviewBinding
import com.smonhof.foodtracker.fragments.recyclerViews.DayListItemRecyclerViewAdapter
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

class MonthOverviewFragment : Fragment(){
    private var _binding : FragmentMonthoverviewBinding? = null
    private val binding get() = _binding!!

    private var yearMonth :YearMonth = YearMonth.now()

    private var days :Set<LocalDate> = emptySet()
    private var dayContents : Set<ThumbEatingDay> = emptySet()

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        _binding = FragmentMonthoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerMonthOverview.text = "${yearMonth.month} ${yearMonth.year}"
        binding.buttonPreviousMonth.setOnClickListener{
            yearMonth = yearMonth.minusMonths(1)
            updateContent(it)
        }
        binding.buttonNextMonth.setOnClickListener{
            yearMonth = yearMonth.plusMonths(1)
            updateContent(it)
        }
        if(binding.list is RecyclerView)
        {
            with(binding.list){
                layoutManager = LinearLayoutManager(context)
                adapter = DayListItemRecyclerViewAdapter(emptyList())
            }
        }
        updateContent(view)

    }

    private fun updateContent(view : View) {
        binding.headerMonthOverview.text = "${yearMonth.month.name} ${yearMonth.year}"
        days = LoadDay.getAllSavedDays(view.context.applicationContext).filter {date -> date.monthValue == yearMonth.monthValue && date.year == yearMonth.year}.toSet()
        dayContents = days.mapNotNull { day -> LoadDay.loadThumb(view.context.applicationContext, day) }.toSet()
        val adapter = binding.list.adapter
        if(adapter is DayListItemRecyclerViewAdapter){
            adapter.updateValues(dayContents.toList())
        }
    }

}