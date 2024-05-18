package com.perfomax.flexstats.home.di

import com.perfomax.flexstats.home.presentation.HomeFragment
import dagger.Component

@Component(dependencies = [HomeFeatureDeps::class])
@HomeScope
interface HomeComponent {

    fun inject(newFragment: HomeFragment)

    @Component.Builder
    interface Builder {
        fun addDeps(deps: HomeFeatureDeps): Builder
        fun build(): HomeComponent
    }
}