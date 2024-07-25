package com.perfomax.flexstats.data.repository

import android.content.Context
import android.util.Log
import com.perfomax.flexstats.core.contracts.YANDEX_DIRECT
import com.perfomax.flexstats.core.contracts.YANDEX_METRIKA
import com.perfomax.flexstats.core.utils.dateMinusDays
import com.perfomax.flexstats.core.utils.getDaysDiapason
import com.perfomax.flexstats.core.utils.isNotMaxUpdateDate
import com.perfomax.flexstats.core.utils.toDateList
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.network.YandexMetrikaStatsNetwork
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.data_api.storage.StatsStorage
import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.GeneralStats
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val DEFAULT_MAX_UPDATE_MONTHS = 6

class StatsRepositoryImpl @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val yandexDirectStatsNetwork: YandexDirectStatsNetwork,
    private val yandexMetrikaStatsNetwork: YandexMetrikaStatsNetwork,
    private val statsStorage: StatsStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): StatsRepository {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val yesterdayDate = LocalDateTime.now().minusDays(1).format(formatter)

    override suspend fun updateStats(updatePeriod: Pair<String, String>): Unit = withContext(dispatcher) {

        val accountsList = accountsRepository.getAllByUser()
        val projectId = accountsList.first().projectId
        accountsList.forEach { account ->
            dataUpdate(account = account, project_id = projectId?:0, updatePeriod = updatePeriod)
        }
        dataProcessing(projectId = projectId?:0)
    }

    override suspend fun getGeneralStats(statsPeriod: Pair<String, String>): List<GeneralStats> {
        val accountsList = accountsRepository.getAllByUser()
        val projectId = accountsList.first().projectId
        return statsStorage.getGeneral(project_id = projectId?:0, stats_period = statsPeriod)
    }
    private suspend fun dataUpdate(account: Account, project_id: Int, updatePeriod: Pair<String, String>) {
        if (account.accountType == YANDEX_DIRECT) {
            for (updateDate in updatePeriod.toDateList()) {
                yandexDirectUpdate(updateDate = updateDate, account = account, project_id = project_id)
            }
        }
        if (account.accountType == YANDEX_METRIKA) {
            for (updateDate in updatePeriod.toDateList()) {
                yandexMetrikaUpdate(updateDate = updateDate, account = account, project_id = project_id)
            }
        }
    }

    private suspend fun dataProcessing(projectId: Int) {
        val firstUpdateDate = statsStorage.getFirstUpdateDateGeneral(projectId)
        val daysDiapason = firstUpdateDate.getDaysDiapason(yesterdayDate)
        for(date in 1..daysDiapason) {
            val updateDate = LocalDateTime.now().minusDays(date.toLong()).format(formatter)
            val yandexDidectDate = statsStorage.getYD(date = updateDate, project_id = projectId)
            val yandexMetrikaDate = statsStorage.getYM(date = updateDate, project_id = projectId)
            if (updateDate.isNotMaxUpdateDate(DEFAULT_MAX_UPDATE_MONTHS)){
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
        }
    }

    private suspend fun yandexDirectUpdate(updateDate: String, account: Account, project_id: Int){
        val yandexDirectStats = yandexDirectStatsNetwork.getStats(
            date = updateDate,
            account = account.name,
            token = account.accountToken?:"",
            projectId = project_id
        )
        statsStorage.addYandexDirectData(data = yandexDirectStats)
    }

    private suspend fun yandexMetrikaUpdate(updateDate: String, account: Account, project_id: Int){
        val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
            date = updateDate,
            account = account.name,
            metrikaCounter = account.metrikaCounter?:"",
            token = account.accountToken?:"",
            projectId = project_id
        )
        statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
    }

    override suspend fun clearStats() {
        val accountsList = accountsRepository.getAllByUser()
        val projectId = accountsList.first().projectId
        statsStorage.clearStats(project_id = projectId?:0)
    }

}