package com.perfomax.flexstats.auth.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.perfomax.auth.R
import com.perfomax.auth.databinding.FragmentResetBinding
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.auth.di.AuthFeatureDepsProvider
import com.perfomax.flexstats.auth.di.DaggerAuthComponent
import com.perfomax.flexstats.core.navigation.Router
import javax.inject.Inject

class ResetFragment: Fragment(R.layout.fragment_reset) {

    companion object {
        fun getInstance(): ResetFragment = ResetFragment()
    }

    private lateinit var binding: FragmentResetBinding

    @Inject
    lateinit var vmFactory: ResetViewModelFactory

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi

    private val resetViewModel by viewModels<ResetViewModel> {
        vmFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeComponent = DaggerAuthComponent
            .builder()
            .addDeps(AuthFeatureDepsProvider.deps)
            .build()
        homeComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResetBinding.bind(view)
        binding.apply {
            resetButton.setOnClickListener {

            }
            toLoginText.setOnClickListener {
                resetViewModel.showLoginScreen()
            }
        }
        setScreen()
    }

    // navigation space-----------------------------------------------------------------------------
    private fun setScreen() {
        resetViewModel.resetScreen.observe(viewLifecycleOwner) {
            when(it) {
                is ResetScreen.Login -> showLoginScreen()
                is ResetScreen.Back -> toBackFragment()
            }
        }
    }

    private fun showLoginScreen() {
        router.navigateTo(
            fragment = authFeatureApi.openLogin(),
            addToBackStack = true
        )
    }

    private fun toBackFragment(){
        router.back()
    }
}