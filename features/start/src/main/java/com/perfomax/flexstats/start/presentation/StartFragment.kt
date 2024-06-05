package com.perfomax.flexstats.start.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.start.di.DaggerStartComponent
import com.perfomax.flexstats.start.di.StartFeatureDepsProvider
import com.perfomax.start.R
import com.perfomax.start.databinding.FragmentStartBinding
import javax.inject.Inject

class StartFragment: Fragment(R.layout.fragment_start)  {

    companion object {
        fun getInstance(): StartFragment = StartFragment()
    }

    private lateinit var binding: FragmentStartBinding

    @Inject
    lateinit var vmFactory: StartViewModelFactory

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi

    @Inject
    lateinit var homeFeatureApi: HomeFeatureApi

    private val startViewModel by viewModels<StartViewModel> {
        vmFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startComponent = DaggerStartComponent
            .builder()
            .addDeps(StartFeatureDepsProvider.deps)
            .build()
        startComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)
        Handler(Looper.getMainLooper()).postDelayed({
            setScreen()
        }, 100)
    }

    // navigation space-----------------------------------------------------------------------------
    private fun setScreen() {
        startViewModel.startScreen.observe(viewLifecycleOwner) {
            when(it) {
                is StartScreen.Login -> showLoginScreen()
                is StartScreen.Home -> showHomeScreen()
            }
        }
    }

    private fun showHomeScreen() {
        router.navigateTo(fragment = homeFeatureApi.open())
    }

    private fun showLoginScreen() {
        router.navigateTo(fragment = authFeatureApi.openLogin())
    }

}