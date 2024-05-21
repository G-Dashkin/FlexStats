package com.perfomax.flexstats.auth.di

import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.core.navigation.Router

interface AuthFeatureDeps {
    val authFeatureApi: AuthFeatureApi
    val homeFeatureApi: HomeFeatureApi
    val router: Router
}