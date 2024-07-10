package com.perfomax.flexstats.data.repository

import android.util.Log
import com.perfomax.flexstats.core.utils.YANDEX_DIRECT
import com.perfomax.flexstats.core.utils.YANDEX_METRIKA
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.network.YandexMetrikaStatsNetwork
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.data_api.storage.StatsStorage
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val yandexDirectStatsNetwork: YandexDirectStatsNetwork,
    private val yandexMetrikaStatsNetwork: YandexMetrikaStatsNetwork,
    private val statsStorage: StatsStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): StatsRepository {

    private val defaultDate = "2024-02-28"

    override suspend fun updateStats(): Unit = withContext(dispatcher) {

        val accountsList = accountsRepository.getAllByUser()
        val projectId = accountsList.first().projectId
        accountsList.forEach { account ->
            if (account.accountType == YANDEX_DIRECT) {
                val yandexDirectStats = yandexDirectStatsNetwork.getStats(
                    date = defaultDate,
                    account = account.name,
                    token = account.accountToken ?:"",
                    projectId = projectId?:0
                )
                Log.d("MyLog", "yandexMetrikaStats in updateStats(): $yandexDirectStats")
                statsStorage.addYandexDirectData(data = yandexDirectStats)
            }

            if (account.accountType == YANDEX_METRIKA) {
                val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
                    date = defaultDate,
                    metrikaCounter = account.metrikaCounter?:"",
                    token = account.accountToken?:"",
                    projectId = projectId?:0
                )
                Log.d("MyLog", "yandexMetrikaStats in updateStats(): $yandexMetrikaStats")
                statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
            }
        }


        dataProcessing(updateDate = defaultDate, projectId = projectId?:0)
    }

    override suspend fun getYandexDirectStats() {
//        statsStorage.getYD()
    }

    override suspend fun getYandexMetrikaStats() {
//        statsStorage.getYM()
    }

    override suspend fun getGeneralStats() {
        statsStorage.getGeneral()
    }

    override suspend fun dataProcessing(updateDate: String, projectId: Int) {
        val yandexDidectDate = statsStorage.getYD(date = updateDate, project_id = projectId)
        val yandexMetrikaDate = statsStorage.getYM(date = updateDate, project_id = projectId)
        Log.d("MyLog", "dataProcessing():1________________________________________________")
        Log.d("MyLog", "yandexDidectDate: $yandexDidectDate")
        Log.d("MyLog", "yandexMetrikaDate: $yandexMetrikaDate")


        val generalStats = GeneralStats(
            date = updateDate,
            cost = yandexDidectDate.map { it.cost }.sumOf { it?:0 },
            impressions = yandexDidectDate.map { it.impressions }.sumOf { it?:0 },
            clicks = yandexDidectDate.map { it.clicks }.sumOf { it?:0 },
            transactions = yandexMetrikaDate.map { it.transactions }.sumOf { it?:0 },
            revenue = yandexMetrikaDate.map { it.revenue }.sumOf { it?:0 },
            project_id = projectId
        )
        Log.d("MyLog", "dataProcessing():2________________________________________________")
        Log.d("MyLog", generalStats.toString())
        statsStorage.addGeneralData(data = generalStats)
    }

    override suspend fun getStats(): List<YandexDirectStats> = withContext(dispatcher) {
        return@withContext listOf<YandexDirectStats>()
    }
}