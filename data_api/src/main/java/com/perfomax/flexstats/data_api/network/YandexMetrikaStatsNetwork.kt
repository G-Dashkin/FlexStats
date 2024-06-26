package com.perfomax.flexstats.data_api.network

import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats


interface YandexMetrikaStatsNetwork {
    suspend fun getStats(date: String, metrikaCounter: String, token: String): YandexMetrikaStats
}