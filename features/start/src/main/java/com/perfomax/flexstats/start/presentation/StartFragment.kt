package com.perfomax.flexstats.start.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.start.di.DaggerStartComponent
import com.perfomax.flexstats.start.di.StartFeatureDepsProvider
import com.perfomax.start.R
import com.perfomax.start.databinding.FragmentStartBinding
import javax.inject.Inject

class StartFragment: Fragment(R.layout.fragment_start)  {

    private lateinit var binding: FragmentStartBinding

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeComponent = DaggerStartComponent
            .builder()
            .addDeps(StartFeatureDepsProvider.deps)
            .build()
        homeComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)
    }

    companion object {
        fun getInstance(): StartFragment = StartFragment()
    }
}