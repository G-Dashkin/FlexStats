package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.GeneralStats

interface StatsRepository {
    suspend fun updateStats()
    suspend fun updateStatsInBackgroundStart()
    suspend fun updateStatsInBackgroundStop()
    suspend fun getGeneralStats(statsPeriod: Pair<String, String>): List<GeneralStats>
    suspend fun clearStats()
}