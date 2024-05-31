package com.perfomax.flexstats.data.storage

import androidx.room.Insert
import com.perfomax.flexstats.data.database.dao.AuthDao
import com.perfomax.flexstats.data.database.dao.ProjectsDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.data_api.storage.ProjectsStorage
import com.perfomax.flexstats.models.Project
import javax.inject.Inject

class ProjectsStorageImpl @Inject constructor(
    private val projectsDao: ProjectsDao
): ProjectsStorage {
    override suspend fun add(project: Project) = projectsDao.insert(project.toDomain())
    override suspend fun rename(projectId: Int, userId: Int) {
        projectsDao.rename(projectId = projectId.toString())
    }

    override suspend fun delete(projectId: Int) {
        projectsDao.delete(projectId = projectId.toString())
    }

    override suspend fun selectProject(projectId: Int, userId: Int) {
        projectsDao.selectProject(userId = userId.toString(), projectId = projectId.toString())
    }

    override suspend fun getSelectedProject(userId: Int): Project {
        return projectsDao.getSelectedProject(userId.toString()).toDomain()
    }
    override suspend fun getAllProjects(): List<Project> {
        return projectsDao.getAllProjects().map { it.toDomain() }
    }
}