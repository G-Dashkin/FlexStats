package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.Project

interface ProjectsStorage {
    suspend fun add(project: Project)
    suspend fun edit(projectId: Int, editName: String)
    suspend fun delete(projectId: Int, userId: Int)
    suspend fun selectProject(projectId: Int)
    suspend fun getSelectedProject(userId: Int): Project
    suspend fun getAllUserProjects(userId: Int): List<Project>
}