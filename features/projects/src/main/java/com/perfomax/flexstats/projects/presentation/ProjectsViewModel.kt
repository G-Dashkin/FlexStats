package com.perfomax.flexstats.projects.presentation

import android.util.Log
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

class ProjectsViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val getAuthUserUseCase: GetAuthUserUseCase
): ViewModel()  {


    fun testGetProjectUserCase(){
        viewModelScope.launch {
            Log.d("MyLog", getProjectsUseCase.execute().toString())
        }
    }

    fun testCreateProjectUseCase(){
        viewModelScope.launch {
            val user = getAuthUserUseCase.execute()
            createProjectUseCase.execute(Project(name = "test Project", userId = user.id?:-1))
        }
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