package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.YandexDirectStats

interface YandexDirectStatsStorage {
    suspend fun add(project: YandexDirectStats)
    suspend fun getAllUserProjects(): List<YandexDirectStats>
}