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

sealed class LoginScreen {
    data object AddNewProject : LoginScreen()
    data object DeleteProject : LoginScreen()
    data object EditProject : LoginScreen()
    data object Back : LoginScreen()
    data object Nothing : LoginScreen()
}
class ProjectsViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val getAuthUserUseCase: GetAuthUserUseCase
): ViewModel()  {

    private val _projectsList = MutableLiveData<List<Project>>()
    val projectsList: LiveData<List<Project>> = _projectsList

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val student = getProjectsUseCase.execute()
            _projectsList.postValue(student)
        }
    }

    fun addNewProject(){
        viewModelScope.launch {
            val user = getAuthUserUseCase.execute()
            createProjectUseCase.execute(Project(name = "test Project", userId = user.id?:-1))
        }
    }


    fun projectClicked(studentId: Int) {

    }

}

class ProjectsViewModelFactory @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val getAuthUserUseCase: GetAuthUserUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return ProjectsViewModel(
            getProjectsUseCase = getProjectsUseCase,
            createProjectUseCase = createProjectUseCase,
            getAuthUserUseCase = getAuthUserUseCase
        ) as T
    }
}