package com.perfomax.flexstats.accounts.di

import com.perfomax.flexstats.api.AccountsFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.data_api.repository.AccountsRepository

interface AccountsFeatureDeps {
    val accountsFeatureApi: AccountsFeatureApi
    val accountsRepository: AccountsRepository
    val router: Router
}