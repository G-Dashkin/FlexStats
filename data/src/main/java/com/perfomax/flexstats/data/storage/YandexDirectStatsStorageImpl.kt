package com.perfomax.flexstats.data.storage

import com.perfomax.flexstats.data.database.dao.YandexDirectStatsDao
import com.perfomax.flexstats.data.database.entities.YandexDirectStatsEntity
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.data_api.storage.YandexDirectStatsStorage
import com.perfomax.flexstats.models.YandexDirectStats
import javax.inject.Inject

class YandexDirectStatsStorageImpl @Inject constructor(
    private val yandexDirectStatsDao: YandexDirectStatsDao
): YandexDirectStatsStorage {

    override suspend fun add(project: YandexDirectStats) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUserProjects(): List<YandexDirectStats> {
        return yandexDirectStatsDao.getData().map { it.toDomain() }
    }
}