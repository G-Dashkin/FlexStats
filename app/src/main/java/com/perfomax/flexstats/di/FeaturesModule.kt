package com.perfomax.flexstats.di

import com.perfomax.flexstats.accounts.api.AccountsFeatureImpl
import com.perfomax.flexstats.api.AccountsFeatureApi
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.home.api.HomeFeatureImpl
import dagger.Binds
import dagger.Module

@Module
interface FeaturesModule {

    @Binds
    fun bindHomeFeature(featureApi: HomeFeatureImpl): HomeFeatureApi

    @Binds
    fun bindAccountsFeature(featureApi: AccountsFeatureImpl): AccountsFeatureApi

}