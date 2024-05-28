package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.Project

interface ProjectsRepository {
    suspend fun create(newProject: Project)
    suspend fun remove(project: Project)
    suspend fun getAll(): List<Project>
}