package com.perfomax.flexstats.home.api

import androidx.fragment.app.Fragment
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.home.presentation.HomeFragment
import javax.inject.Inject

class HomeFeatureImpl @Inject constructor() : HomeFeatureApi {
    override fun open(): Fragment = HomeFragment.getInstance()
}