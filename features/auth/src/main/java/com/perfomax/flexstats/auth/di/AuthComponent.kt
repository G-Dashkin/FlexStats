package com.perfomax.flexstats.auth.di

import com.perfomax.flexstats.auth.presentation.LoginFragment
import com.perfomax.flexstats.auth.presentation.RegisterFragment
import com.perfomax.flexstats.auth.presentation.ResetFragment
import dagger.Component

@Component(dependencies = [AuthFeatureDeps::class])
@AuthScope
interface AuthComponent {
    fun inject(newFragment: LoginFragment)
    fun inject(newFragment: RegisterFragment)
    fun inject(newFragment: ResetFragment)

    @Component.Builder
    interface Builder {
        fun addDeps(deps: AuthFeatureDeps): Builder
        fun build(): AuthComponent
    }
}