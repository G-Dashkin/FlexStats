package com.perfomax.flexstats.data.repository

import android.util.Log
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.data_api.storage.ProjectsStorage
import com.perfomax.flexstats.models.Project
import javax.inject.Inject

class ProjectsRepositoryImpl @Inject constructor(
    private val projectsStorage: ProjectsStorage,
    private val authStorage: AuthStorage
): ProjectsRepository {
    override suspend fun create(project: Project){
        val authUserId = authStorage.getAuthUser().id
        projectsStorage.add(Project(name = project.name, userId = authUserId))
    }

    override suspend fun remove(project: Project) = projectsStorage.remove(project)

    override suspend fun getAll(): List<Project> = projectsStorage.getAllProjects()
}