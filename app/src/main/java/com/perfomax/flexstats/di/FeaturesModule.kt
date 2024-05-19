package com.perfomax.flexstats.di

import com.perfomax.flexstats.accounts.api.AccountsFeatureImpl
import com.perfomax.flexstats.api.AccountsFeatureApi
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.api.ProjectsFeatureApi
import com.perfomax.flexstats.api.StartFeatureApi
import com.perfomax.flexstats.auth.api.AuthFeatureImpl
import com.perfomax.flexstats.home.api.HomeFeatureImpl
import com.perfomax.flexstats.projects.api.ProjectsFeatureImpl
import com.perfomax.flexstats.start.api.StartFeatureImpl
import dagger.Binds
import dagger.Module

@Module
interface FeaturesModule {

    @Binds
    fun bindStartFeature(featureApi: StartFeatureImpl): StartFeatureApi

    @Binds
    fun bindAuthFeature(featureApi: AuthFeatureImpl): AuthFeatureApi

    @Binds
    fun bindHomeFeature(featureApi: HomeFeatureImpl): HomeFeatureApi

    @Binds
    fun bindProjectsFeature(featureApi: ProjectsFeatureImpl): ProjectsFeatureApi

    @Binds
    fun bindAccountsFeature(featureApi: AccountsFeatureImpl): AccountsFeatureApi

}