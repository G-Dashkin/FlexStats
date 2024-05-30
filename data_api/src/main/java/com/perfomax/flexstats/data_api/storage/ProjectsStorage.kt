package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.User

interface ProjectsStorage {
    suspend fun add(project: Project)
    suspend fun remove(project: Project)
    suspend fun getSelectedProject(): Project
    suspend fun getAllProjects(): List<Project>
}