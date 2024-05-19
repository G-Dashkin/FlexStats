package com.perfomax.flexstats.app

import android.app.Application
import com.perfomax.flexstats.accounts.di.AccountsFeatureDepsProvider
import com.perfomax.flexstats.auth.di.AuthFeatureDepsProvider
import com.perfomax.flexstats.di.AppModule
import com.perfomax.flexstats.di.DaggerAppComponent
import com.perfomax.flexstats.di.DaggerProvider
import com.perfomax.flexstats.home.di.HomeFeatureDepsProvider
import com.perfomax.flexstats.projects.di.ProjectsFeatureDepsProvider
import com.perfomax.flexstats.start.di.StartFeatureDepsProvider

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initDagger()
        instance = this
    }

    private fun initDagger() {
        val appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
        DaggerProvider.appComponent = appComponent
        StartFeatureDepsProvider.deps = appComponent
        AuthFeatureDepsProvider.deps = appComponent
        HomeFeatureDepsProvider.deps = appComponent
        ProjectsFeatureDepsProvider.deps = appComponent
        AccountsFeatureDepsProvider.deps = appComponent
    }
    companion object {
        lateinit var instance: App private set
    }
}