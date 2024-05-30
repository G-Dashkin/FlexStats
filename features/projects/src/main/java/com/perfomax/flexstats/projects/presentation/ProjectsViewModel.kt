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
import com.perfomax.flexstats.projects.domain.usecases.GetAuthUserUseCase
import com.perfomax.flexstats.projects.domain.usecases.GetProjectsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProjectsScreen {
    data object AddNewProject : ProjectsScreen()
    data class DeleteProject(val projectId: Int) : ProjectsScreen()
    data class EditProject(val projectId: Int) : ProjectsScreen()
    data object Nothing : ProjectsScreen()
}
class ProjectsViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase
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
        }
    }


    fun projectClicked(projectId: Int) {


    }

    fun editProject(projectId: Int){
        Log.d("MyLog", "editProject id: $projectId")
        _projectsScreen.value = ProjectsScreen.EditProject(projectId)
    }
    fun deleteProject(projectId: Int){
        Log.d("MyLog", "deleteProject id: $projectId")
        _projectsScreen.value = ProjectsScreen.DeleteProject(projectId)
    }

}

class ProjectsViewModelFactory @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return ProjectsViewModel(
            getProjectsUseCase = getProjectsUseCase,
            createProjectUseCase = createProjectUseCase
        ) as T
    }
}