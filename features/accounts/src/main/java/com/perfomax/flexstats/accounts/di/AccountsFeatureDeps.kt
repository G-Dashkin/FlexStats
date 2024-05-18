package com.perfomax.flexstats.accounts.di

import com.perfomax.flexstats.api.AccountsFeatureApi
import com.perfomax.flexstats.core.navigation.Router

interface AccountsFeatureDeps {
    val accountsFeatureApi: AccountsFeatureApi
    val router: Router
}