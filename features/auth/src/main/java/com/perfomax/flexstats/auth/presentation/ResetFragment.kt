package com.perfomax.flexstats.auth.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.perfomax.auth.R
import com.perfomax.auth.databinding.FragmentRegisterBinding
import com.perfomax.auth.databinding.FragmentResetBinding
import com.perfomax.flexstats.auth.di.AuthFeatureDepsProvider
import com.perfomax.flexstats.auth.di.DaggerAuthComponent
import com.perfomax.flexstats.core.navigation.Router
import javax.inject.Inject

class ResetFragment: Fragment(R.layout.fragment_reset) {

    private lateinit var binding: FragmentResetBinding

    @Inject
    lateinit var router: Router

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
    }


    companion object {
        fun getInstance(): ResetFragment = ResetFragment()
    }
}