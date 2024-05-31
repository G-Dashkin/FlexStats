package com.perfomax.flexstats.data.repository

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
        val authedUserId = authStorage.getAuthedUser().id
        projectsStorage.add(Project(name = project.name, userId = authedUserId))
    }

    override suspend fun delete(projectId: Int) {
        val authedUserId = authStorage.getAuthedUser().id
        projectsStorage.delete(projectId = projectId, userId = authedUserId?:0)
    }

    override suspend fun select(projectId: Int) {
        val authedUserId = authStorage.getAuthedUser().id
        projectsStorage.selectProject(projectId = projectId, userId = authedUserId?:0)
    }

    override suspend fun getSelected(): Project {
        val authedUserId = authStorage.getAuthedUser().id
        return projectsStorage.getSelectedProject(userId = authedUserId?:0)
    }

    override suspend fun getAll(): List<Project> = projectsStorage.getAllProjects()
}