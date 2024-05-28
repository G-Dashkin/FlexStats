package com.perfomax.flexstats.data.repository

import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.data_api.storage.ProjectsStorage
import com.perfomax.flexstats.models.Project
import javax.inject.Inject

class ProjectsRepositoryImpl @Inject constructor(
    private val projectsStorage: ProjectsStorage
): ProjectsRepository {
    override suspend fun create(project: Project) = projectsStorage.add(project)

    override suspend fun remove(project: Project) = projectsStorage.remove(project)

    override suspend fun getAll(): List<Project> = projectsStorage.getAllProjects()
}