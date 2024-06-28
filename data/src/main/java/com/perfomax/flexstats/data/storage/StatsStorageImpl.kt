package com.perfomax.flexstats.data.storage

import android.util.Log
import com.perfomax.flexstats.data.database.dao.StatsDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.data_api.storage.StatsStorage
import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats
import javax.inject.Inject

class StatsStorageImpl @Inject constructor(
    private val statsDao: StatsDao
): StatsStorage {

    override suspend fun addYandexDirectData(data: YandexDirectStats) {
        Log.d("MyLog", data.toString())
        statsDao.removeYandexDirectData(
            date = data.date?:"",
            projectId = data.project_id?:0
        )
        statsDao.insertYandexDirectData(data = data.toDomain() )
    }

    override suspend fun addYandexMetrikaData(data: YandexMetrikaStats) {
        statsDao.removeYandexMetrikaData(
            date = data.date?:"",
            projectId = data.project_id?:0
        )
        statsDao.insertYandexMetrikaData(data = data.toDomain() )
    }

    override suspend fun getYD() {
        val yandexDirectData = statsDao.getYandexDirectData()
        Log.d("MyLog", "Direct Data-------------------------------------------------------")
        yandexDirectData.forEach { Log.d("MyLog", it.toString()) }
    }

    override suspend fun getYM() {
        val yandexMetrikaData = statsDao.getYandexMetrikaData()
        Log.d("MyLog", "Metrika Data------------------------------------------------------")
        yandexMetrikaData.forEach { Log.d("MyLog", it.toString()) }
    }

    override suspend fun getGeneral() {
        val generalData = statsDao.getGeneralData()
        Log.d("MyLog", "General Data------------------------------------------------------")
        generalData.forEach { Log.d("MyLog", it.toString()) }
    }
}