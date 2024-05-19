package com.perfomax.flexstats.projects.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.projects.di.DaggerProjectsComponent
import com.perfomax.flexstats.projects.di.ProjectsFeatureDepsProvider
import com.perfomax.projects.R
import com.perfomax.projects.databinding.FragmentProjectsBinding
import javax.inject.Inject

class ProjectsFragment: Fragment(R.layout.fragment_projects) {

    private lateinit var binding: FragmentProjectsBinding

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeComponent = DaggerProjectsComponent
            .builder()
            .addDeps(ProjectsFeatureDepsProvider.deps)
            .build()
        homeComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectsBinding.bind(view)
    }

    companion object {
        fun getInstance(): ProjectsFragment = ProjectsFragment()
    }
}