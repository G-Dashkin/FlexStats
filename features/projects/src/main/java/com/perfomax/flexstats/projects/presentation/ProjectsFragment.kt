package com.perfomax.flexstats.projects.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.core.utils.CALL_MENU_LISTENER
import com.perfomax.flexstats.projects.di.DaggerProjectsComponent
import com.perfomax.flexstats.projects.di.ProjectsFeatureDepsProvider
import com.perfomax.projects.R
import com.perfomax.projects.databinding.CustomDialogBinding
import com.perfomax.projects.databinding.FragmentProjectsBinding
import com.perfomax.projects.databinding.ProjectDialogBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProjectsFragment: Fragment(R.layout.fragment_projects) {

    companion object {
        fun getInstance(): ProjectsFragment = ProjectsFragment()
    }

    private lateinit var binding: FragmentProjectsBinding
    private lateinit var bindingCustomDialog: CustomDialogBinding
    private lateinit var projectDialogBinding: ProjectDialogBinding

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
            projectsViewModel.showAddProjectDialog()
        }
        setAdapter()
        setScreen()
    }

    private fun setAdapter() {
        val adapter = ProjectsAdapter(
            itemProjectClick = {
                lifecycleScope.launch {
                    projectsViewModel.selectProject(it)
                    delay(100)
                    parentFragmentManager.setFragmentResult(CALL_MENU_LISTENER, bundleOf())
                }
            },
            editProjectClick = { projectId, currentName ->
                projectsViewModel.showEditProjectDialog(projectId = projectId, currentName = currentName)
            },
            deleteProjectClick = { projectId, projectName ->
                projectsViewModel.showDeleteProjectDialog(projectId = projectId, projectName = projectName)
            }
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
                is ProjectsScreen.AddNewProject -> showProjectDialog()
                is ProjectsScreen.SelectProject -> {}
                is ProjectsScreen.ProjectExists -> projectExists()
                is ProjectsScreen.EmptyProject -> emptyProject()
                is ProjectsScreen.EditProject -> {
                    lifecycleScope.launch {
                        showProjectDialog(projectId = it.projectId, currentName = it.currentName)
                        delay(100)
                        parentFragmentManager.setFragmentResult(CALL_MENU_LISTENER, bundleOf())
                    }
                }
                is ProjectsScreen.DeleteProject -> {
                    showDeleteProjectDialog(projectId = it.projectId, projectName = it.projectName)
                }
                is ProjectsScreen.Nothing -> {}
            }
        }
    }

    private fun showProjectDialog(projectId: Int? = null, currentName: String? = null) {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        projectDialogBinding = ProjectDialogBinding.inflate(inflater)
        dialog.setContentView(projectDialogBinding.root)
        dialog.show()

        if (projectId != null) projectDialogBinding.projectNameForm.hint = currentName
        projectDialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        projectDialogBinding.btnConfirm.setOnClickListener {
            val projectName = projectDialogBinding.projectNameForm.text
            if (projectName.isEmpty()) projectsViewModel.projectEmpty()
            else if (projectsViewModel.projectsList.value?.any { it.name == projectName.toString() } == true) projectsViewModel.projectExists()
            else {
                lifecycleScope.launch {
                    val projectName = projectDialogBinding.projectNameForm.text
                    if (projectId != null) projectsViewModel.editProject(projectId = projectId, editName = projectName.toString())
                    else projectsViewModel.addNewProject(projectName = projectName.toString())
                    delay(100)
                    parentFragmentManager.setFragmentResult(CALL_MENU_LISTENER, bundleOf())
                    dialog.dismiss()
                }
            }
        }
    }

    private fun showDeleteProjectDialog(projectId: Int, projectName: String) {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        bindingCustomDialog = CustomDialogBinding.inflate(inflater)
        dialog.setContentView(bindingCustomDialog.root)
        dialog.show()
        bindingCustomDialog.text2.text = projectName
        bindingCustomDialog.btnCancel.setOnClickListener { dialog.dismiss() }
        bindingCustomDialog.btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                projectsViewModel.deleteProjectClicked(projectId)
                delay(100)
                parentFragmentManager.setFragmentResult(CALL_MENU_LISTENER, bundleOf())
                dialog.dismiss()
            }
        }
    }

    private fun settingsDialog(): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        return dialog
    }

    // Toast space-----------------------------------------------------------------------------
    private fun emptyProject(){
        Toast.makeText(activity, "Поле \"Проект\" должно быть заполнено", Toast.LENGTH_LONG).show()
    }

    private fun projectExists(){
        Toast.makeText(activity, "Проект с таким названием уже существует", Toast.LENGTH_LONG).show()
    }
}