package com.perfomax.flexstats.data_api.network

import com.perfomax.flexstats.models.YandexDirectStats


interface YandexDirectStatsNetwork {
    suspend fun getStats(
        date: String,
        account: String,
        token: String,
        projectId: Int
    ): YandexDirectStats
}