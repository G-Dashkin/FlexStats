package com.perfomax.flexstats.data.repository

import android.util.Log
import com.perfomax.flexstats.data.database.dao.YandexDirectStatsDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.models.YandexDirectStats
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val yandexDirectStatsNetwork: YandexDirectStatsNetwork,
    private val yandexDirectStatsDao: YandexDirectStatsDao,
    private val accountsRepository: AccountsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): StatsRepository {
    override suspend fun loadStats(): Unit = withContext(dispatcher) {
        val accountsList = accountsRepository.getAllByUser()
        accountsList.forEach { account ->
            yandexDirectStatsNetwork.getStats(
                date = "2024-02-29",
                account = account.name,
                token = account.accountToken?:""
            )
        }
    }

    override suspend fun getStats(): List<YandexDirectStats> = withContext(dispatcher) {
        return@withContext yandexDirectStatsDao.getData().map { it.toDomain() }
    }
}