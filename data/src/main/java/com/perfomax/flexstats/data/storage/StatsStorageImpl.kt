package com.perfomax.flexstats.data.storage

import com.perfomax.flexstats.core.contracts.DATE_FORMAT
import com.perfomax.flexstats.core.contracts.EMPTY
import com.perfomax.flexstats.data.database.dao.StatsDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.data_api.storage.StatsStorage
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class StatsStorageImpl @Inject constructor(
    private val statsDao: StatsDao
): StatsStorage {

    override suspend fun addYandexDirectData(data: YandexDirectStats) {
        statsDao.removeYandexDirectData(
            date = data.date?:EMPTY,
            projectId = data.project_id?:0,
            account = data.account?:EMPTY
        )
        statsDao.insertYandexDirectData(data = data.toDomain() )
    }

    override suspend fun addYandexMetrikaData(data: YandexMetrikaStats) {
        statsDao.removeYandexMetrikaData(
            date = data.date?: EMPTY,
            projectId = data.project_id?:0,
            counter = data.counter?: EMPTY
        )
        statsDao.insertYandexMetrikaData(data = data.toDomain())
    }

    override suspend fun addGeneralData(data: GeneralStats) {
        statsDao.removeGeneralData(
            date = data.date?: EMPTY,
            projectId = data.project_id?:0
        )
        statsDao.insertGeneralData(data = data.toDomain())
    }


    override suspend fun getYandexDirectData(date: String, project_id: Int): List<YandexDirectStats> {
        val yandexDirectData = statsDao.getYandexDirectData(date = date, projectId = project_id)
        return yandexDirectData.map { it.toDomain() }
    }

    override suspend fun getYandexMetrikaData(date: String, project_id: Int): List<YandexMetrikaStats> {
        val yandexMetrikaData = statsDao.getYandexMetrikaData(date = date, projectId = project_id)
        return yandexMetrikaData.map { it.toDomain() }
    }

    override suspend fun getGeneralData(project_id: Int, statsPeriod: Pair<String, String>): List<GeneralStats> {
        return statsDao.getGeneralData(
            projectId = project_id,
            firstDate = statsPeriod.first,
            secondDate = statsPeriod.second
        ).map { it.toDomain() }
    }

    override suspend fun checkAccountYandexDirect(account: String, project_id: Int): Boolean {
        val checkedDate = statsDao.getLastDateYandexDirectByAccount(account = account, projectId = project_id)
        return checkedDate.isNotEmpty()
    }

    override suspend fun checkCounterYandexMetrika(counter: String, project_id: Int): Boolean {
        val checkedDate = statsDao.getLastDateYandexMetrikaByCounter(counter = counter, projectId = project_id)
        return checkedDate.isNotEmpty()
    }

    override suspend fun getFirstUpdateDateYandexDirect(account: String, project_id: Int): String {
        val yandexDirectDateByAccount = statsDao.getFirstDateYandexDirectByAccount(account = account, projectId = project_id)
        return yandexDirectDateByAccount.first().date
    }

    override suspend fun getFirstUpdateDateYandexMetrika(counter: String, project_id: Int): String {
        val yandexMetrikaDateByCounter = statsDao.getFirstDateYandexMetrikaByCounter(counter = counter, projectId = project_id)
        return yandexMetrikaDateByCounter.first().date
    }

    override suspend fun getFirstUpdateDateGeneral(project_id: Int): String {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        val yandexMetrikaDate = SimpleDateFormat(DATE_FORMAT).parse(
            statsDao.getFirstDateYandexMetrikaByCounter(projectId = project_id).first().date
        )
        val yandexDirectDate = SimpleDateFormat(DATE_FORMAT).parse(
            statsDao.getFirstDateYandexDirectByAccount(projectId = project_id).first().date)
        val minDate = listOf(yandexMetrikaDate,yandexDirectDate).min().time
        return simpleDateFormat.format(minDate)
    }


    override suspend fun getLastUpdateDateYandexDirect(account: String, project_id: Int): String {
        val yandexDirectDateByAccount = statsDao.getLastDateYandexDirectByAccount(account = account, projectId = project_id)
        return yandexDirectDateByAccount.first().date
    }

    override suspend fun getLastUpdateDateYandexMetrika(counter: String, project_id: Int): String {
        val yandexMetrikaDateByCounter = statsDao.getLastDateYandexMetrikaByCounter(counter = counter, projectId = project_id)
        return yandexMetrikaDateByCounter.first().date
    }

    override suspend fun getLastUpdateDateGeneral(project_id: Int): String {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        val yandexMetrikaDate = SimpleDateFormat(DATE_FORMAT).parse(
            statsDao.getLastDateYandexDirectByAccount(projectId = project_id).first().date
        )
        val yandexDirectDate = SimpleDateFormat(DATE_FORMAT).parse(
            statsDao.getLastDateYandexMetrikaByCounter(projectId = project_id).first().date)
        val minDate = listOf(yandexMetrikaDate,yandexDirectDate).max().time
        return simpleDateFormat.format(minDate)
    }

    override suspend fun clearStats(project_id: Int) {
        statsDao.clearYandexDirect(project_id)
        statsDao.clearYandexMetrika(project_id)
        statsDao.clearGeneral(project_id)
    }

}