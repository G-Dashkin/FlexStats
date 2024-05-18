package com.perfomax.flexstats.home.di

import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.core.navigation.Router

interface HomeFeatureDeps {
    val homeFeatureApi: HomeFeatureApi
    val router: Router
}