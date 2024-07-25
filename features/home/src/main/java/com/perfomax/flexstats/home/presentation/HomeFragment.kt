package com.perfomax.flexstats.home.presentation

import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.core.utils.toTimestamp
import com.perfomax.flexstats.home.di.DaggerHomeComponent
import com.perfomax.flexstats.home.di.HomeFeatureDepsProvider
import com.perfomax.home.R
import com.perfomax.home.databinding.FragmentHomeBinding
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

        binding.selectStatsPeriodButton.setOnClickListener {
            homeViewModel.showStatsDatePiker()
        }

        binding.selectUpdatePeriodButton.setOnClickListener {
            homeViewModel.showUpdateDataPiker()
        }

        binding.updateStatsButton.setOnClickListener {
            homeViewModel.updateStats()
        }

        binding.clearStatsButton.setOnClickListener {
            homeViewModel.clearStats()
        }
        setAdapter()
        setScreen()
    }

    private fun setScreen() {
        homeViewModel.homeScreen.observe(viewLifecycleOwner) {
            when(it) {
                is HomeScreen.ShowTitle -> binding.twTitle.visibility = View.VISIBLE
                is HomeScreen.HideTitle -> binding.twTitle.visibility = View.GONE
                is HomeScreen.ShowProgressIndicator -> showProgressIndicator()
                is HomeScreen.HideProgressIndicator -> hideProgressIndicator()
                is HomeScreen.ShowDatePicker -> showStatsDatePiker()
                is HomeScreen.ShowUpdatePicker -> showUpdateDataPiker()
            }
        }
    }

    private fun setAdapter() {
        val adapter = StatsAdapter()
        binding.statsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.statsRecyclerView.adapter = adapter
        homeViewModel.statsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun showStatsDatePiker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(com.perfomax.ui.R.style.MaterialCalendarTheme)
            .setTitleText("Выберети период отображения статистики")
            .setSelection(
                Pair(
                    homeViewModel.selectedStatsPeriod.value?.first?.toTimestamp(),
                    homeViewModel.selectedStatsPeriod.value?.second?.toTimestamp(),
                )
            )
            .build()

        picker.show(this.parentFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener {
            homeViewModel.selectStatsPeriod(
                firstDate = homeViewModel.convertTimeToDate(it.first),
                secondDate = homeViewModel.convertTimeToDate(it.second)
            )
        }
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
    }

    private fun showUpdateDataPiker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(com.perfomax.ui.R.style.MaterialCalendarTheme)
            .setTitleText("Выберети период обновления статистики статистики")
            .setSelection(
                Pair(
                    homeViewModel.selectedUpdateStatsPeriod.value?.first?.toTimestamp(),
                    homeViewModel.selectedUpdateStatsPeriod.value?.second?.toTimestamp(),
                )
            )
            .build()

        picker.show(this.parentFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener {
            homeViewModel.selectUpdatePeriod(
                firstDate = homeViewModel.convertTimeToDate(it.first),
                secondDate = homeViewModel.convertTimeToDate(it.second)
            )
        }
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
    }

    private fun showProgressIndicator() {
        binding.circularProgressIndicator.visibility = View.VISIBLE
        binding.circularProgressIndicator.bringToFront()
        binding.updateStatsButton.isEnabled = false
        binding.selectStatsPeriodButton.isEnabled = false
        binding.selectUpdatePeriodButton.isEnabled = false
        binding.updateStatsButton.isEnabled = false
        binding.updateStatsButton.isEnabled = false
        binding.clearStatsButton.isEnabled = false
    }

    private fun hideProgressIndicator() {
        binding.circularProgressIndicator.visibility = View.GONE
        binding.updateStatsButton.isEnabled = true
        binding.selectStatsPeriodButton.isEnabled = true
        binding.selectUpdatePeriodButton.isEnabled = true
        binding.updateStatsButton.isEnabled = true
        binding.selectUpdatePeriodButton.isEnabled = true
        binding.clearStatsButton.isEnabled = true
    }

    companion object {
        fun getInstance(): HomeFragment = HomeFragment()
    }
}

