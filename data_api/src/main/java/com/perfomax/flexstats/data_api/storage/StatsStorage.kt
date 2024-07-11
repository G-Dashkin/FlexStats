package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats

interface StatsStorage {

    suspend fun addYandexDirectData(data: YandexDirectStats)
    suspend fun addYandexMetrikaData(data: YandexMetrikaStats)
    suspend fun addGeneralData(data: GeneralStats)
    suspend fun getYD(date: String, project_id: Int): List<YandexDirectStats>
    suspend fun checkAccountYD(account: String, project_id: Int): Boolean
    suspend fun checkCounterYM(counter: String, project_id: Int): Boolean
    suspend fun getLastUpdateDateYD(account: String, project_id: Int): String
    suspend fun getLastUpdateDateYM(counter: String, project_id: Int): String
    suspend fun getYM(date: String, project_id: Int): List<YandexMetrikaStats>
    suspend fun getGeneral(): List<GeneralStats>

}