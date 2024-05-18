package com.perfomax.flexstats.accounts.di

import com.perfomax.flexstats.accounts.presentation.AccountsFragment
import dagger.Component

@Component(dependencies = [AccountsFeatureDeps::class])
@AccountsScope
interface AccountsComponent {

    fun inject(newFragment: AccountsFragment)

    @Component.Builder
    interface Builder {
        fun addDeps(deps: AccountsFeatureDeps): Builder
        fun build(): AccountsComponent
    }
}