package com.perfomax.flexstats.accounts.di

import com.perfomax.flexstats.api.AccountsFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.ProjectsRepository

interface AccountsFeatureDeps {
    val accountsFeatureApi: AccountsFeatureApi
    val accountsRepository: AccountsRepository
    val projectsRepository: ProjectsRepository
    val router: Router
}