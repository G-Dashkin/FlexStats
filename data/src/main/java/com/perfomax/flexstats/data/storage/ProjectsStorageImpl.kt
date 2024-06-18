package com.perfomax.flexstats.data.storage

import com.perfomax.flexstats.core.utils.EMPTY
import com.perfomax.flexstats.data.database.dao.ProjectsDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.data_api.storage.ProjectsStorage
import com.perfomax.flexstats.models.Project
import java.lang.Exception
import javax.inject.Inject

class ProjectsStorageImpl @Inject constructor(
    private val projectsDao: ProjectsDao
): ProjectsStorage {
    override suspend fun add(project: Project) {
        projectsDao.resectSelectedProjects(userId = project.userId.toString())
        projectsDao.insert(project.toDomain())
    }
    override suspend fun edit(projectId: Int, editName: String) {
        projectsDao.edit(projectId = projectId.toString(), editName = editName)
    }
    override suspend fun delete(projectId: Int) {
        projectsDao.delete(projectId = projectId.toString())
        projectsDao.selectProject(projectId = (projectId-1).toString())
    }

    override suspend fun selectProject(projectId: Int) {
        projectsDao.selectProject(projectId = projectId.toString())
    }

    override suspend fun getSelectedProject(userId: Int): Project {
        val selectedProject = projectsDao.getSelectedProject(userId.toString())
        return try {
            selectedProject.toDomain()
        } catch (e:Exception) {
            Project(0, EMPTY,true,0)
        }
    }
    override suspend fun getAllUserProjects(userId: Int): List<Project> {
        return projectsDao.getAllProjectsOfUser(userId.toString()).map { it.toDomain() }
    }
}