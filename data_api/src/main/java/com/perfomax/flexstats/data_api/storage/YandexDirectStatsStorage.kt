package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.Stats

interface YandexDirectStatsStorage {
    suspend fun add(project: Stats)
    suspend fun getAllUserProjects(userId: Int): List<Stats>
}