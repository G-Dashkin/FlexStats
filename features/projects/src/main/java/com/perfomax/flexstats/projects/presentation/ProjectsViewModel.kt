package com.perfomax.flexstats.projects.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.projects.domain.usecases.CreateProjectUseCase
import com.perfomax.flexstats.projects.domain.usecases.DeleteProjectUseCase
import com.perfomax.flexstats.projects.domain.usecases.EditProjectUseCase
import com.perfomax.flexstats.projects.domain.usecases.GetUserProjectsUseCase
import com.perfomax.flexstats.projects.domain.usecases.SelectProjectUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProjectsScreen {
    data object AddNewProject : ProjectsScreen()
    data class SelectProject(val projectId: Int) : ProjectsScreen()
    data class DeleteProject(val projectId: Int, val projectName: String) : ProjectsScreen()
    data class EditProject(val projectId: Int, val currentName: String) : ProjectsScreen()
    data object EmptyProject : ProjectsScreen()
    data object ProjectExists : ProjectsScreen()
    data object Nothing : ProjectsScreen()
}
class ProjectsViewModel(
    private val getUserProjectsUseCase: GetUserProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val editProjectUseCase: EditProjectUseCase,
    private val selectProjectUseCase: SelectProjectUseCase,
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
            val student = getUserProjectsUseCase.execute()
            _projectsList.postValue(student)
        }
    }

    fun addNewProject(projectName: String){
        viewModelScope.launch {
            createProjectUseCase.execute(Project(name = projectName))
            load()
        }

    }

    fun projectEmpty(){
        _projectsScreen.value = ProjectsScreen.EmptyProject
    }

    fun projectExists() {
        _projectsScreen.value = ProjectsScreen.ProjectExists
    }

    fun editProject(projectId: Int, editName: String){
        viewModelScope.launch {
            editProjectUseCase.execute(Project(id = projectId, name = editName))
            load()
        }
    }


    fun selectProject(projectId: Int) {
        viewModelScope.launch {
            selectProjectUseCase.execute(projectId)
            load()
        }
    }

    fun showAddProjectDialog(){
        _projectsScreen.value = ProjectsScreen.AddNewProject
    }

    fun showEditProjectDialog(projectId: Int, currentName: String){
        _projectsScreen.value = ProjectsScreen.EditProject(projectId = projectId, currentName = currentName)
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
    private val getUserProjectsUseCase: GetUserProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val editProjectUseCase: EditProjectUseCase,
    private val selectProjectUseCase: SelectProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return ProjectsViewModel(
            getUserProjectsUseCase = getUserProjectsUseCase,
            createProjectUseCase = createProjectUseCase,
            editProjectUseCase= editProjectUseCase,
            selectProjectUseCase = selectProjectUseCase,
            deleteProjectUseCase = deleteProjectUseCase
        ) as T
    }
}