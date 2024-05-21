package com.perfomax.flexstats.start.di

import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.api.StartFeatureApi
import com.perfomax.flexstats.core.navigation.Router

interface StartFeatureDeps {
    val startFeatureApi: StartFeatureApi
    val authFeatureApi: AuthFeatureApi
    val router: Router
}