package com.perfomax.flexstats.projects.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.projects.di.DaggerProjectsComponent
import com.perfomax.flexstats.projects.di.ProjectsFeatureDepsProvider
import com.perfomax.projects.R
import com.perfomax.projects.databinding.AddProjectDialogBinding
import com.perfomax.projects.databinding.CustomDialogBinding
import com.perfomax.projects.databinding.FragmentProjectsBinding
import javax.inject.Inject

class ProjectsFragment: Fragment(R.layout.fragment_projects) {

    companion object {
        fun getInstance(): ProjectsFragment = ProjectsFragment()
    }

    private lateinit var binding: FragmentProjectsBinding
    private lateinit var bindingCustomDialog: CustomDialogBinding
    private lateinit var addProjectDialogBinding: AddProjectDialogBinding

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
        binding.btnAddProjects.setOnClickListener { showAddProjectDialog() }
        setAdapter()
        setScreen()
    }

    private fun setAdapter() {
        val adapter = ProjectsAdapter(
            itemProjectClick = { projectsViewModel.selectProject(it) },
            editProjectClick = { projectsViewModel.editProject(it) },
            deleteProjectClick = { projectsViewModel.showDeleteProjectDialog(it) }
        )
        binding.projectsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.projectsRecyclerView.adapter = adapter

        projectsViewModel.projectsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    // AlertDialogs---------------------------------------------------------------------------------
    private fun setScreen() {
        projectsViewModel.projectsScreen.observe(viewLifecycleOwner) {
            when(it) {
                is ProjectsScreen.AddNewProject -> {}
                is ProjectsScreen.SelectProject -> {}
                is ProjectsScreen.EditProject -> showEditProjectDialog(it.projectId)
                is ProjectsScreen.DeleteProject -> showDeleteProjectDialog(it.projectId)
                is ProjectsScreen.Nothing -> {}
            }
        }
    }

    private fun showAddProjectDialog() {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        addProjectDialogBinding = AddProjectDialogBinding.inflate(inflater)
        dialog.setContentView(addProjectDialogBinding.root)
        dialog.show()
        addProjectDialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        addProjectDialogBinding.btnConfirm.setOnClickListener {
            val projectName = addProjectDialogBinding.projectNameForm.text
            projectsViewModel.addNewProject(projectName = projectName.toString())
            dialog.dismiss()
        }
    }

    private fun showEditProjectDialog(projectId: Int) {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        addProjectDialogBinding = AddProjectDialogBinding.inflate(inflater)
        dialog.setContentView(addProjectDialogBinding.root)
        dialog.show()
        addProjectDialogBinding.projectNameText.text = "Id проекта $projectId"
        addProjectDialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        addProjectDialogBinding.btnConfirm.setOnClickListener { dialog.dismiss() }
    }

    private fun showDeleteProjectDialog(projectId: Int) {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        bindingCustomDialog = CustomDialogBinding.inflate(inflater)
        dialog.setContentView(bindingCustomDialog.root)
        dialog.show()
        bindingCustomDialog.text2.text = bindingCustomDialog.toString()
        bindingCustomDialog.btnCancel.setOnClickListener { dialog.dismiss() }
        bindingCustomDialog.btnConfirm.setOnClickListener {
            projectsViewModel.deleteProjectClicked(projectId)
            dialog.dismiss()
        }
    }

    private fun settingsDialog(): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        return dialog
    }
}