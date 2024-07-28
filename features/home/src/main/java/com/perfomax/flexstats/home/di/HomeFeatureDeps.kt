package com.perfomax.flexstats.home.di

import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository

interface HomeFeatureDeps {
    val homeFeatureApi: HomeFeatureApi
    val statsRepository: StatsRepository
    val accountsRepository: AccountsRepository
    val router: Router
}