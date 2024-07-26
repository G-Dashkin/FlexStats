package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.GeneralStats
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    suspend fun updateStats(updatePeriod: Pair<String, String>)
    suspend fun getGeneralStats(statsPeriod: Pair<String, String>): List<GeneralStats>
    suspend fun clearStats()
    suspend fun testFlow(): Flow<String>
}