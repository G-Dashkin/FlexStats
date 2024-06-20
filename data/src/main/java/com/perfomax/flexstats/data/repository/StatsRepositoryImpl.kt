package com.perfomax.flexstats.data.repository

import com.perfomax.flexstats.data_api.network.YandexAccessTokenNetwork
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.repository.StatsRepository
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val yandexDirectStatsNetwork: YandexDirectStatsNetwork
): StatsRepository {
    override suspend fun getStats() {
        yandexDirectStatsNetwork.getStats(
            date = "2024-03-20",
            account = "citilink-brand",
            token = "AgAAAAA8H0K8AAMfZT3JgEnJC0qYoJdfwMHXav0"
        )
    }
}