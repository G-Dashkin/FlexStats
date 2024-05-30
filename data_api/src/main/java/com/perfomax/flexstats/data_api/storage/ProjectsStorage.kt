package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.User

interface ProjectsStorage {
    suspend fun add(project: Project)
    suspend fun remove(project: Project)
    suspend fun selectProject(projectId: Int, userId: Int)
    suspend fun getSelectedProject(userId: Int): Project
    suspend fun getAllProjects(): List<Project>
}