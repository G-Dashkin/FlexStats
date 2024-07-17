package com.perfomax.flexstats.data.storage

import com.perfomax.flexstats.data.database.dao.StatsDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.data_api.storage.StatsStorage
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats
import javax.inject.Inject

class StatsStorageImpl @Inject constructor(
    private val statsDao: StatsDao
): StatsStorage {

    override suspend fun addYandexDirectData(data: YandexDirectStats) {
        statsDao.removeYandexDirectData(
            date = data.date?:"",
            projectId = data.project_id?:0,
            account = data.account?:""
        )
        statsDao.insertYandexDirectData(data = data.toDomain() )
    }

    override suspend fun addYandexMetrikaData(data: YandexMetrikaStats) {
        statsDao.removeYandexMetrikaData(
            date = data.date?:"",
            projectId = data.project_id?:0,
            counter = data.counter?:""
        )
        statsDao.insertYandexMetrikaData(data = data.toDomain() )
    }

    override suspend fun addGeneralData(data: GeneralStats) {
        statsDao.removeGeneralData(
            date = data.date?:"",
            projectId = data.project_id?:0
        )
        statsDao.insertGeneralData(data = data.toDomain())
    }


    override suspend fun checkAccountYD(account: String, project_id: Int): Boolean {
        val checkedDate = statsDao.getLastDateYDByAccount(account = account, projectId = project_id)
        return checkedDate.isNotEmpty()
    }

    override suspend fun checkCounterYM(counter: String, project_id: Int): Boolean {
        val checkedDate = statsDao.getLastDateYMByCounter(counter = counter, projectId = project_id)
        return checkedDate.isNotEmpty()
    }

    override suspend fun getLastUpdateDateYD(account: String, project_id: Int): String {
        val yandexDirectDateByAccount = statsDao.getLastDateYDByAccount(account = account, projectId = project_id)
        return yandexDirectDateByAccount.first().date
    }

    override suspend fun getFirstUpdateDateYD(account: String, project_id: Int): String {
        val yandexDirectDateByAccount = statsDao.getFirstDateYDByAccount(account = account, projectId = project_id)
        return yandexDirectDateByAccount.first().date
    }

    override suspend fun getLastUpdateDateYM(counter: String, project_id: Int): String {
        val yandexMetrikaDateByCounter = statsDao.getLastDateYMByCounter(counter = counter, projectId = project_id)
        return yandexMetrikaDateByCounter.first().date
    }

    override suspend fun getFirstUpdateDateYM(counter: String, project_id: Int): String {
        val yandexMetrikaDateByCounter = statsDao.getFirstDateYMByCounter(counter = counter, projectId = project_id)
        return yandexMetrikaDateByCounter.first().date
    }

    override suspend fun getYD(date: String, project_id: Int): List<YandexDirectStats> {
        val yandexDirectData = statsDao.getYandexDirectData(date = date, projectId = project_id)
        return yandexDirectData.map { it.toDomain() }
    }

    override suspend fun getYM(date: String, project_id: Int): List<YandexMetrikaStats> {
        val yandexMetrikaData = statsDao.getYandexMetrikaData(date = date, projectId = project_id)
        return yandexMetrikaData.map { it.toDomain() }
    }

    override suspend fun getGeneral(project_id: Int, statsPeriod: Pair<String, String>): List<GeneralStats> {
        return statsDao.getGeneralData(
            projectId = project_id,
            firstDate = statsPeriod.first,
            secondDate = statsPeriod.second
        ).map { it.toDomain() }
    }
}