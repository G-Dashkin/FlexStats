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
        val authedUserId = authStorage.getAuthUser().id
        projectsStorage.add(Project(name = project.name, userId = authedUserId))
    }

    override suspend fun edit(projectId: Int, editName: String) {
        projectsStorage.edit(projectId = projectId, editName = editName)
    }

    override suspend fun delete(projectId: Int) {
        projectsStorage.delete(projectId = projectId)
    }

    override suspend fun select(projectId: Int) {
        projectsStorage.selectProject(projectId = projectId)
    }

    override suspend fun getSelected(): Project {
        val authedUserId = authStorage.getAuthUser().id
        return projectsStorage.getSelectedProject(userId = authedUserId?:0)
    }

    override suspend fun getAll(): List<Project> = projectsStorage.getAllProjects()
}