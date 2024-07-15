package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats

interface StatsRepository {
    suspend fun updateStats()
    suspend fun getGeneralStats(statsPeriod: Pair<String, String>): List<GeneralStats>
    suspend fun dataProcessing(updateDate: String, projectId: Int)
    suspend fun getStats(): List<YandexDirectStats>
}