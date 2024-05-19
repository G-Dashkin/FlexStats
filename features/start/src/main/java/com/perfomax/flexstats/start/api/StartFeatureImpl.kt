package com.perfomax.flexstats.start.api

import androidx.fragment.app.Fragment
import com.perfomax.flexstats.api.StartFeatureApi
import com.perfomax.flexstats.start.presentation.StartFragment
import javax.inject.Inject

class  StartFeatureImpl @Inject constructor() : StartFeatureApi {
    override fun open(): Fragment = StartFragment.getInstance()
}