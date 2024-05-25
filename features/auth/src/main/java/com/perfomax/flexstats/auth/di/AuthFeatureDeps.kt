package com.perfomax.flexstats.auth.di

import android.content.Context
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flextats.data_api.repository.AuthRepository

interface AuthFeatureDeps {
    val context: Context
    val authRepository: AuthRepository
    val authFeatureApi: AuthFeatureApi
    val homeFeatureApi: HomeFeatureApi
    val router: Router
}