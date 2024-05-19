package com.perfomax.flexstats.projects.di

import com.perfomax.flexstats.api.ProjectsFeatureApi
import com.perfomax.flexstats.core.navigation.Router

interface ProjectsFeatureDeps {
    val projectsFeatureApi: ProjectsFeatureApi
    val router: Router
}