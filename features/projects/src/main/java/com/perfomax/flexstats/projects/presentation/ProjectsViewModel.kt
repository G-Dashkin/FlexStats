package com.perfomax.flexstats.projects.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.projects.domain.usecases.CreateProjectUseCase
import com.perfomax.flexstats.projects.domain.usecases.DeleteProjectUseCase
import com.perfomax.flexstats.projects.domain.usecases.GetProjectsUseCase
import com.perfomax.flexstats.projects.domain.usecases.GetSelectedProjectUseCase
import com.perfomax.flexstats.projects.domain.usecases.SelectProjectUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProjectsScreen {
    data object AddNewProject : ProjectsScreen()
    data class SelectProject(val projectId: Int) : ProjectsScreen()
    data class DeleteProject(val projectId: Int, val projectName: String) : ProjectsScreen()
    data class EditProject(val projectId: Int) : ProjectsScreen()
    data object Nothing : ProjectsScreen()
}
class ProjectsViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val selectProjectUseCase: SelectProjectUseCase,
    private val getSelectedProjectUseCase: GetSelectedProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase
): ViewModel()  {

    private val _projectsList = MutableLiveData<List<Project>>()
    val projectsList: LiveData<List<Project>> = _projectsList

    private val _projectsScreen = MutableLiveData<ProjectsScreen>()
    val projectsScreen: LiveData<ProjectsScreen> = _projectsScreen

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val student = getProjectsUseCase.execute()
            _projectsList.postValue(student)
        }
    }

    fun addNewProject(projectName: String){
        viewModelScope.launch {
            createProjectUseCase.execute(Project(name = projectName))
            load()
        }
    }


    fun selectProject(projectId: Int) {
        viewModelScope.launch {
            selectProjectUseCase.execute(projectId)
            load()
        }
    }

    fun editProject(projectId: Int){
        _projectsScreen.value = ProjectsScreen.EditProject(projectId)
    }
    fun showDeleteProjectDialog(projectId: Int, projectName: String){
        _projectsScreen.value = ProjectsScreen.DeleteProject(projectId = projectId, projectName = projectName)
    }

    fun deleteProjectClicked(projectId: Int){
        viewModelScope.launch {
            deleteProjectUseCase.execute(projectId)
            load()
        }
    }
}

class ProjectsViewModelFactory @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val selectProjectUseCase: SelectProjectUseCase,
    private val getSelectedProjectUseCase: GetSelectedProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return ProjectsViewModel(
            getProjectsUseCase = getProjectsUseCase,
            createProjectUseCase = createProjectUseCase,
            selectProjectUseCase = selectProjectUseCase,
            getSelectedProjectUseCase = getSelectedProjectUseCase,
            deleteProjectUseCase = deleteProjectUseCase
        ) as T
    }
}