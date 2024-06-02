package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.Project

interface ProjectsRepository {
    suspend fun create(project: Project)
    suspend fun edit(projectId: Int, editName: String)
    suspend fun delete(projectId: Int)
    suspend fun select(projectId: Int)
    suspend fun getSelected(): Project
    suspend fun getUserAll(): List<Project>
}