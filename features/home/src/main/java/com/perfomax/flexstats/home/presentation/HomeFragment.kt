package com.perfomax.flexstats.home.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.home.di.DaggerHomeComponent
import com.perfomax.flexstats.home.di.HomeFeatureDepsProvider
import com.perfomax.home.R
import com.perfomax.home.databinding.FragmentHomeBinding
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var router: Router

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
    }

    companion object {
        fun getInstance(): HomeFragment = HomeFragment()
    }
}

