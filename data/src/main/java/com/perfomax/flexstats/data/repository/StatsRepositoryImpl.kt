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
            date = "2024-02-29",
            account = "imedia-citilink-xiaomi-v",
            token = "y0_AgAAAAB0dtmjAAug-QAAAAEGAAJdAABqeL6XcxlKELlweD9gwhjAmKbzMQ"

//                    account = "imedia-citilink-xiaomi-v",
//            token = "y0_AgAAAAB0dtmjAAug-QAAAAEGAAJdAABqeL6XcxlKELlweD9gwhjAmKbzMQ"
        )
    }
}