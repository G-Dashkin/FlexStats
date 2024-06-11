package com.perfomax.flexstats.accounts.di

import com.perfomax.flexstats.accounts.presentation.AccountsFragment
import com.perfomax.flexstats.accounts.presentation.YandexDirectListFragment
import com.perfomax.flexstats.accounts.presentation.YandexMetrikaListFragment
import dagger.Component

@Component(dependencies = [AccountsFeatureDeps::class])
@AccountsScope
interface AccountsComponent {

    fun inject(newFragment: AccountsFragment)
    fun inject(newFragment: YandexDirectListFragment)
    fun inject(newFragment: YandexMetrikaListFragment)

    @Component.Builder
    interface Builder {
        fun addDeps(deps: AccountsFeatureDeps): Builder
        fun build(): AccountsComponent
    }
}