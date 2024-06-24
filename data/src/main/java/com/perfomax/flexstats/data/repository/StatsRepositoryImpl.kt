package com.perfomax.flexstats.data.repository

import android.util.Log
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.network.YandexMetrikaStatsNetwork
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.data_api.storage.StatsStorage
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

    private val defaultDate = "2024-02-29"

    override suspend fun updateStats(): Unit = withContext(dispatcher) {

        val accountsList = accountsRepository.getAllByUser()

        // выгрузка яндкес директ
        accountsList.forEach { account ->
            // выгрузка по апи, получение статистики по аккаунту
            if (account.accountType == "yandex_direct") {
                val yandexDirectStats = yandexDirectStatsNetwork.getStats(
                    date = defaultDate,
                    account = account.name,
                    token = account.accountToken ?: ""
                )
                Log.d("MyLog", "Update Stats--------------------------------------------------")
                yandexDirectStats.forEach { Log.d("MyLog", it.toString()) }
                // Загрузка в таблицу yandex_direct (через сторадж)
                statsStorage.addYandexDirectData(yandexDirectStats)
            }

            if (account.accountType == "yandex_metrika") {

                yandexMetrikaStatsNetwork.getStats(
                    date = defaultDate,
                    metrikaCounter = account.metrikaCounter?:"",
                    token = account.accountToken?:""
                )
            }
        }
        dataProcessing()
    }
        // -------------------------------------------------------------------------------------
        // выгрузка яндкес метрика
        // counterList.forEach {}

        // Вызов внутреннего метода для загрузки в "general_stats"




    override suspend fun getYandexDirectStats() {
        statsStorage.getYD()
    }

    override suspend fun getYandexMetrikaStats() {
        statsStorage.getYM()
    }

    override suspend fun getGeneralStats() {
        statsStorage.getGeneral()
    }

    override suspend fun dataProcessing() {
//        val yandexDidectDate = statsStorage.getYD()
//        val yandexMetrikaDate = statsStorage.getYM()

    }

    override suspend fun getStats(): List<YandexDirectStats> = withContext(dispatcher) {
//        Log.d("MyLog", statsDao.getData().toString())
//        return@withContext yandexDirectStatsDao.getData().map { it.toDomain() }
        return@withContext listOf<YandexDirectStats>()
    }
}