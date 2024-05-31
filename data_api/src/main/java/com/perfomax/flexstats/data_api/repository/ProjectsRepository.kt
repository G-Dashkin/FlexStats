package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.Project

interface ProjectsRepository {
    suspend fun create(project: Project)
    suspend fun delete(project: Int)
    suspend fun select(project: Int)
    suspend fun getSelected(): Project
    suspend fun getAll(): List<Project>
}