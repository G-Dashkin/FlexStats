package com.perfomax.flexstats.projects.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.projects.di.DaggerProjectsComponent
import com.perfomax.flexstats.projects.di.ProjectsFeatureDepsProvider
import com.perfomax.projects.R
import com.perfomax.projects.databinding.FragmentProjectsBinding
import javax.inject.Inject

class ProjectsFragment: Fragment(R.layout.fragment_projects) {

    companion object {
        fun getInstance(): ProjectsFragment = ProjectsFragment()
    }

    private lateinit var binding: FragmentProjectsBinding

    @Inject
    lateinit var vmFactory: ProjectsViewModelFactory

    @Inject
    lateinit var router: Router

    private val projectsViewModel by viewModels<ProjectsViewModel> {
        vmFactory
    }

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
        binding.btnAddProjects.setOnClickListener {
            projectsViewModel.addNewProject()
        }
        setAdapter()
    }

    private fun setAdapter() {
        val adapter = ProjectsAdapter {
            projectsViewModel.projectClicked(it)
        }
        binding.projectsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.projectsRecyclerView.adapter = adapter

        projectsViewModel.projectsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


}