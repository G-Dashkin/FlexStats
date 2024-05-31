package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.Project

interface ProjectsStorage {
    suspend fun add(project: Project)
    suspend fun rename(projectId: Int, userId: Int)
    suspend fun delete(projectId: Int)
    suspend fun selectProject(projectId: Int, userId: Int)
    suspend fun getSelectedProject(userId: Int): Project
    suspend fun getAllProjects(): List<Project>
}