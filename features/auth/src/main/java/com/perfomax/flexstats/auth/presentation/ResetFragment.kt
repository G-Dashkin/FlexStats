package com.perfomax.flexstats.auth.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.perfomax.auth.R
import com.perfomax.auth.databinding.FragmentRegisterBinding
import com.perfomax.auth.databinding.FragmentResetBinding
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.auth.di.AuthFeatureDepsProvider
import com.perfomax.flexstats.auth.di.DaggerAuthComponent
import com.perfomax.flexstats.core.navigation.Router
import javax.inject.Inject

class ResetFragment: Fragment(R.layout.fragment_reset) {

    private lateinit var binding: FragmentResetBinding

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

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
                router.navigateTo(
                    fragment = authFeatureApi.openLogin(),
                    addToBackStack = true
                )
            }
        }
    }


    companion object {
        fun getInstance(): ResetFragment = ResetFragment()
    }
}