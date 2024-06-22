package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.YandexDirectStats

interface StatsRepository {
    suspend fun loadStats()
    suspend fun getStats(): List<YandexDirectStats>
}