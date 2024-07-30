package com.perfomax.flexstats.data.repository

import android.content.Context
import com.perfomax.flexstats.core.contracts.DATE_FORMAT
import com.perfomax.flexstats.core.contracts.EMPTY
import com.perfomax.flexstats.core.contracts.YANDEX_DIRECT
import com.perfomax.flexstats.core.contracts.YANDEX_METRIKA
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val context: Context
): StatsRepository {

    private val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    private val yesterdayDate = LocalDateTime.now().minusDays(1).format(formatter)

    override suspend fun updateStats(updatePeriod: Pair<String, String>): Flow<String> = withContext(dispatcher)  {
        flow {
            val accountsList = accountsRepository.getAllAccountsByUser()
            val projectId = accountsList.first().projectId
            accountsList.forEach { account ->
                dataUpdate(account = account, project_id = projectId?:0, updatePeriod = updatePeriod).collect{
                    emit(it)
                }
            }
            dataProcessing(projectId = projectId?:0)
        }.flowOn(dispatcher)
    }

    override suspend fun getGeneralStats(statsPeriod: Pair<String, String>): List<GeneralStats> {
        val accountsList = accountsRepository.getAllAccountsByUser()
        return if (accountsList.isNotEmpty()){
            val projectId = accountsList.first().projectId
            statsStorage.getGeneralData(project_id = projectId?:0, stats_period = statsPeriod)
        } else listOf()
    }

    override suspend fun clearStats() {
        val accountsList = accountsRepository.getAllAccountsByUser()
        val projectId = accountsList.first().projectId
        statsStorage.clearStats(project_id = projectId?:0)
    }

    private suspend fun dataUpdate(account: Account, project_id: Int, updatePeriod: Pair<String, String>): Flow<String>  {
            return flow {
                if (account.accountType == YANDEX_DIRECT) {
                    for (updateDate in updatePeriod.toDateList()) {
                        emit("$updateDate | ${account.name}")
                        yandexDirectUpdate(updateDate = updateDate, account = account, project_id = project_id)
                    }
                }
                if (account.accountType == YANDEX_METRIKA) {
                    for (updateDate in updatePeriod.toDateList()) {

                        emit("$updateDate | ${account.name} | " +
                                "${context.resources.getString(com.perfomax.ui.R.string.counter)} " +
                                "${account.metrikaCounter}")
                        yandexMetrikaUpdate(updateDate = updateDate, account = account, project_id = project_id)
                    }
                }
        }.flowOn(dispatcher)
    }

    private suspend fun dataProcessing(projectId: Int) {
        val firstUpdateDate = statsStorage.getFirstUpdateDateGeneral(projectId)
        val daysDiapason = firstUpdateDate.getDaysDiapason(yesterdayDate)
        for(date in 1..daysDiapason) {
            val updateDate = LocalDateTime.now().minusDays(date.toLong()).format(formatter)
            val yandexDidectDate = statsStorage.getYandexDirectData(date = updateDate, project_id = projectId)
            val yandexMetrikaDate = statsStorage.getYandexMetrikaData(date = updateDate, project_id = projectId)
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
                val isEmptyData = generalStats.cost?.equals(0L)?:true
                               && generalStats.impressions?.equals(0)?:true
                               && generalStats.clicks?.equals(0)?:true
                               && generalStats.transactions?.equals(0)?:true
                               && generalStats.revenue?.equals(0L)?:true
                if (!isEmptyData) statsStorage.addGeneralData(data = generalStats)
            }
        }
    }

    private suspend fun yandexDirectUpdate(updateDate: String, account: Account, project_id: Int){
        val yandexDirectStats = yandexDirectStatsNetwork.getStats(
            date = updateDate,
            account = account.name,
            token = account.accountToken?:EMPTY,
            projectId = project_id
        )
        statsStorage.addYandexDirectData(data = yandexDirectStats)
    }

    private suspend fun yandexMetrikaUpdate(updateDate: String, account: Account, project_id: Int){
        val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
            date = updateDate,
            account = account.name,
            metrikaCounter = account.metrikaCounter?: EMPTY,
            token = account.accountToken?: EMPTY,
            projectId = project_id
        )
        statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
    }

}