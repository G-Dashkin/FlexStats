package com.perfomax.flexstats.di

import com.perfomax.flexstats.accounts.di.AccountsFeatureDeps
import com.perfomax.flexstats.home.di.HomeFeatureDeps
import com.perfomax.flexstats.presentation.navigation.NavigatorFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, FeaturesModule::class])
interface AppComponent : HomeFeatureDeps, AccountsFeatureDeps {
    fun inject(fragment: NavigatorFragment)
}