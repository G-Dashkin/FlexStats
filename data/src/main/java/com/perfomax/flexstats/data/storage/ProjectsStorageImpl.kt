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
    override suspend fun remove(project: Project) = projectsDao.delete(project.toDomain())
    override suspend fun getAllProjects(): List<Project> = projectsDao.getAllProjects().map { it.toDomain() }
}