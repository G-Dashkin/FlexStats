package com.perfomax.flexstats.home.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.home.di.DaggerHomeComponent
import com.perfomax.flexstats.home.di.HomeFeatureDepsProvider
import com.perfomax.home.R
import com.perfomax.home.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var vmFactory: HomeViewModelFactory

    @Inject
    lateinit var router: Router

    private val homeViewModel by viewModels<HomeViewModel> {
        vmFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeComponent = DaggerHomeComponent
            .builder()
            .addDeps(HomeFeatureDepsProvider.deps)
            .build()
        homeComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.updateStatsButton.setOnClickListener {
            homeViewModel.updateStats()
        }
        binding.selectPeriodButton.setOnClickListener {
            showDatePiker()
        }

        setAdapter()
        setProgressIndicator()
    }

    private fun setAdapter() {
        val adapter = StatsAdapter()
        binding.statsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.statsRecyclerView.adapter = adapter
        homeViewModel.statsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun showDatePiker() {
        val thirtyDays = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        thirtyDays.add(Calendar.DAY_OF_YEAR, -30)
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(com.perfomax.ui.R.style.ThemeMaterialCalendar)
            .setTitleText("Выберети период отображения статистики")
            .setSelection(Pair(thirtyDays.timeInMillis, yesterday.timeInMillis))
            .build()

        picker.show(this.parentFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener {
            homeViewModel.selectStatsPeriod(
                firstDate = convertTimeToDate(it.first),
                secondDate = convertTimeToDate(it.second)
            )
        }
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
    }

    private fun setProgressIndicator() {
        homeViewModel.progressIndicator.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.circularProgressIndicator.visibility = View.VISIBLE
                binding.circularProgressIndicator.bringToFront()
                binding.updateStatsButton.isEnabled = false
                binding.selectPeriodButton.isEnabled = false
            } else {
                binding.circularProgressIndicator.visibility = View.GONE
                binding.updateStatsButton.isEnabled = true
                binding.selectPeriodButton.isEnabled = true
            }
        }
    }

    private fun convertTimeToDate(time:Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(utc.time)
    }

    companion object {
        fun getInstance(): HomeFragment = HomeFragment()
    }
}

