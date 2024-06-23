package com.perfomax.flexstats.home.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.perfomax.flexstats.core.navigation.Router
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

        binding.loadStats.setOnClickListener {
            homeViewModel.loadStats()
        }

        binding.getYandexDirect.setOnClickListener {
            homeViewModel.getYandexDirect()
        }

        binding.getYandexMetrika.setOnClickListener {
            homeViewModel.getYandexMetrika()
        }

        binding.loadGeneral.setOnClickListener {
            homeViewModel.getGeneral()
        }


        setAdapter()
    }

    private fun setAdapter() {
        val adapter = StatsAdapter()
        binding.statsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.statsRecyclerView.adapter = adapter
        homeViewModel.statsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    companion object {
        fun getInstance(): HomeFragment = HomeFragment()
    }
}

