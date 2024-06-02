package com.perfomax.flexstats.projects.di

import com.perfomax.flexstats.api.ProjectsFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.repository.ProjectsRepository

interface ProjectsFeatureDeps {
    val projectsFeatureApi: ProjectsFeatureApi
    val projectsRepository: ProjectsRepository
    val accountsRepository: AccountsRepository
    val router: Router
}