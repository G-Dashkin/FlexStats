package com.perfomax.flexstats.start.di

import com.perfomax.flexstats.start.presentation.StartFragment
import dagger.Component

@Component(dependencies = [StartFeatureDeps::class])
@StartScope
interface StartComponent {
    fun inject(newFragment: StartFragment)

    @Component.Builder
    interface Builder {
        fun addDeps(deps: StartFeatureDeps): Builder
        fun build(): StartComponent
    }
}