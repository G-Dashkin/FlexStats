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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val yandexDirectStatsNetwork: YandexDirectStatsNetwork,
    private val yandexMetrikaStatsNetwork: YandexMetrikaStatsNetwork,
    private val statsStorage: StatsStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): StatsRepository {

    val DEFAULT_UPDATE_DAYS = 7
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    //Добавить логику - если данны обновлены по вчерашний день, то обновлять не нужно
    override suspend fun updateStats(): Unit = withContext(dispatcher) {

        val accountsList = accountsRepository.getAllByUser()
        val projectId = accountsList.first().projectId

        accountsList.forEach { account ->
            if (account.accountType == YANDEX_DIRECT) {
                val isDataByAccountYDExists = statsStorage.checkAccountYD(account = account.name, project_id = projectId?:0)
                if (isDataByAccountYDExists){
                    val lastUpdateDate = statsStorage.getLastUpdateDateYD(account = account.name, project_id = projectId?:0)
                    val yesterdayDate = LocalDateTime.now().minusDays(1).format(formatter)
                    if (lastUpdateDate != yesterdayDate){
                        for(day in 1..updateDays(lastUpdateDate)) {
                            val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                            val yandexDirectStats = yandexDirectStatsNetwork.getStats(
                                date = updateDate,
                                account = account.name,
                                token = account.accountToken ?: "",
                                projectId = projectId ?: 0
                            )
                            statsStorage.addYandexDirectData(data = yandexDirectStats)
                        }
                    }
                } else {
                    for(day in 4..DEFAULT_UPDATE_DAYS) {
                        val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                        val yandexDirectStats = yandexDirectStatsNetwork.getStats(
                            date = updateDate,
                            account = account.name,
                            token = account.accountToken ?: "",
                            projectId = projectId ?: 0
                        )
                        statsStorage.addYandexDirectData(data = yandexDirectStats)
                    }
                }
            }

            //--------------------------------------------------------------------------------------
            if (account.accountType == YANDEX_METRIKA) {
                val isDataByCounterYMExists = statsStorage.checkCounterYM(
                    counter = account.metrikaCounter?:"",
                    project_id = projectId?:0
                )
                if (isDataByCounterYMExists){
                    val lastUpdateDate = statsStorage.getLastUpdateDateYM(
                        counter = account.metrikaCounter?:"",
                        project_id = projectId?:0
                    )
                    val yesterdayDate = LocalDateTime.now().minusDays(1).format(formatter)
                    if (lastUpdateDate != yesterdayDate){
                        for(day in 1..updateDays(lastUpdateDate)) {
                            val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                            val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
                                date = updateDate,
                                metrikaCounter = account.metrikaCounter?:"",
                                token = account.accountToken?:"",
                                projectId = projectId?:0
                            )
                            statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
                        }
                    }
                } else {
                    for(date in 4..DEFAULT_UPDATE_DAYS) {
                        val updateDate = LocalDateTime.now().minusDays(date.toLong()).format(formatter)
                        val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
                            date = updateDate,
                            metrikaCounter = account.metrikaCounter?:"",
                            token = account.accountToken?:"",
                            projectId = projectId?:0
                        )
                        statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
                    }
                }
            }
        }

        for(date in 1..DEFAULT_UPDATE_DAYS) {
            val updateDate = LocalDateTime.now().minusDays(date.toLong()).format(formatter)
            dataProcessing(updateDate = updateDate, projectId = projectId ?: 0)
        }
    }

    override suspend fun getGeneralStats(statsPeriod: Pair<String, String>): List<GeneralStats> {
        val accountsList = accountsRepository.getAllByUser()
        val projectId = accountsList.first().projectId
        return statsStorage.getGeneral(project_id = projectId?:0, stats_period = statsPeriod)
    }

    override suspend fun dataProcessing(updateDate: String, projectId: Int) {
        val yandexDidectDate = statsStorage.getYD(date = updateDate, project_id = projectId)
        val yandexMetrikaDate = statsStorage.getYM(date = updateDate, project_id = projectId)

        val generalStats = GeneralStats(
            date = updateDate,
            cost = yandexDidectDate.map { it.cost }.sumOf { it?:0 },
            impressions = yandexDidectDate.map { it.impressions }.sumOf { it?:0 },
            clicks = yandexDidectDate.map { it.clicks }.sumOf { it?:0 },
            transactions = yandexMetrikaDate.map { it.transactions }.sumOf { it?:0 },
            revenue = yandexMetrikaDate.map { it.revenue }.sumOf { it?:0 },
            project_id = projectId
        )
        statsStorage.addGeneralData(data = generalStats)
    }

    override suspend fun getStats(): List<YandexDirectStats> = withContext(dispatcher) {
        return@withContext listOf<YandexDirectStats>()
    }

    private fun updateDays(lastUpdateDate: String): Int {

        val yesterdayDate = LocalDateTime.now().minusDays(1).format(formatter)
        val date1 = LocalDate.of(
            lastUpdateDate.split("-")[0].toInt(),
            lastUpdateDate.split("-")[1].toInt(),
            lastUpdateDate.split("-")[2].toInt()
        )
        val date2 = LocalDate.of(
            yesterdayDate.split("-")[0].toInt(),
            yesterdayDate.split("-")[1].toInt(),
            yesterdayDate.split("-")[2].toInt()
        )
        return Period.between(date1, date2).days
    }
}