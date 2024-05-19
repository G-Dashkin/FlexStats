package com.perfomax.flexstats.projects.di

import com.perfomax.flexstats.projects.presentation.ProjectsFragment
import dagger.Component

@Component(dependencies = [ProjectsFeatureDeps::class])
@ProjectsScope
interface ProjectsComponent {
    fun inject(newFragment: ProjectsFragment)

    @Component.Builder
    interface Builder {
        fun addDeps(deps: ProjectsFeatureDeps): Builder
        fun build(): ProjectsComponent
    }
}