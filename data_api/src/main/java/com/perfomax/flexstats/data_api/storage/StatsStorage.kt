package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats

interface StatsStorage {

    suspend fun addYandexDirectData(data: YandexDirectStats)
    suspend fun addYandexMetrikaData(data: YandexMetrikaStats)
    suspend fun addGeneralData(data: GeneralStats)

    suspend fun getYD(date: String, project_id: Int): List<YandexDirectStats>
    suspend fun getYM(date: String, project_id: Int): List<YandexMetrikaStats>
    suspend fun getGeneral(project_id: Int, stats_period: Pair<String, String>): List<GeneralStats>

    suspend fun checkAccountYD(account: String, project_id: Int): Boolean
    suspend fun checkCounterYM(counter: String, project_id: Int): Boolean

    suspend fun getFirstUpdateDateYD(account: String, project_id: Int): String
    suspend fun getFirstUpdateDateYM(counter: String, project_id: Int): String
    suspend fun getFirstUpdateDateGeneral(project_id: Int): String

    suspend fun getLastUpdateDateYD(account: String, project_id: Int): String
    suspend fun getLastUpdateDateYM(counter: String, project_id: Int): String
    suspend fun getLastUpdateDateGeneral(project_id: Int): String
}