package com.perfomax.flexstats.di

import com.perfomax.flexstats.accounts.di.AccountsFeatureDeps
import com.perfomax.flexstats.auth.di.AuthFeatureDeps
import com.perfomax.flexstats.home.di.HomeFeatureDeps
import com.perfomax.flexstats.presentation.main.MainActivity
import com.perfomax.flexstats.presentation.navigation.NavigatorFragment
import com.perfomax.flexstats.projects.di.ProjectsFeatureDeps
import com.perfomax.flexstats.start.di.StartFeatureDeps
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, FeaturesModule::class])
interface AppComponent : StartFeatureDeps, AuthFeatureDeps, HomeFeatureDeps, ProjectsFeatureDeps, AccountsFeatureDeps {
    fun inject(fragment: NavigatorFragment)
}