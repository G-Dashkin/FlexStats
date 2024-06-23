package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.YandexDirectStats

interface StatsRepository {
    suspend fun updateStats()
    suspend fun getYandexDirectStats()
    suspend fun getYandexMetrikaStats()
    suspend fun getGeneralStats()
    suspend fun dataProcessing()
    suspend fun getStats(): List<YandexDirectStats>
}