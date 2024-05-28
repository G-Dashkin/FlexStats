package com.perfomax.flexstats.start.di

import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.api.StartFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.data_api.repository.AuthRepository

interface StartFeatureDeps {
    val startFeatureApi: StartFeatureApi
    val authFeatureApi: AuthFeatureApi
    val homeFeatureApi: HomeFeatureApi
    val authRepository: AuthRepository
    val router: Router
}