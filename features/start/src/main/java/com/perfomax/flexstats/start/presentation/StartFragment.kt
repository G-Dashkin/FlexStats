package com.perfomax.flexstats.start.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.perfomax.flexstats.api.AuthFeatureApi
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

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val startComponent = DaggerStartComponent
            .builder()
            .addDeps(StartFeatureDepsProvider.deps)
            .build()
        startComponent.inject(this)

        Handler(Looper.getMainLooper()).postDelayed({
            router.navigateTo(fragment = authFeatureApi.openLogin())
        }, 1000)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)
    }

    companion object {
        fun getInstance(): StartFragment = StartFragment()
    }
}