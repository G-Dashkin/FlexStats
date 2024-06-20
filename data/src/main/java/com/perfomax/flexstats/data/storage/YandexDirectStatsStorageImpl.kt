package com.perfomax.flexstats.data.storage

import com.perfomax.flexstats.data.database.dao.AccountsDao
import com.perfomax.flexstats.data.database.dao.YandexDirectStatsDao
import com.perfomax.flexstats.data_api.storage.AccountsStorage
import com.perfomax.flexstats.data_api.storage.YandexDirectStatsStorage
import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.Stats
import javax.inject.Inject

class YandexDirectStatsStorageImpl @Inject constructor(
    private val yandexDirectStatsDao: YandexDirectStatsDao
): YandexDirectStatsStorage {

    override suspend fun add(project: Stats) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUserProjects(userId: Int): List<Stats> {
        TODO("Not yet implemented")
    }
}