package com.perfomax.flexstats.projects.api

import androidx.fragment.app.Fragment
import com.perfomax.flexstats.api.ProjectsFeatureApi
import com.perfomax.flexstats.projects.presentation.ProjectsFragment
import javax.inject.Inject

class ProjectsFeatureImpl @Inject constructor() : ProjectsFeatureApi {
    override fun open(): Fragment = ProjectsFragment.getInstance()
}