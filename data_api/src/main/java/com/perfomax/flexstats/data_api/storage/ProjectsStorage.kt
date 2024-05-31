package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.Project

interface ProjectsStorage {
    suspend fun add(project: Project)
    suspend fun rename(projectId: Int)
    suspend fun delete(projectId: Int)
    suspend fun selectProject(projectId: Int)
    suspend fun getSelectedProject(userId: Int): Project
    suspend fun getAllProjects(): List<Project>
}