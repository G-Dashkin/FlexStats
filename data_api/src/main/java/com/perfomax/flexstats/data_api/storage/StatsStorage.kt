package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats

interface StatsStorage {
    suspend fun addYandexDirectData(data: YandexDirectStats)
    suspend fun addYandexMetrikaData(data: YandexMetrikaStats)
    suspend fun addGeneralData(data: GeneralStats)
    suspend fun getYandexDirectData(date: String, project_id: Int): List<YandexDirectStats>
    suspend fun getYandexMetrikaData(date: String, project_id: Int): List<YandexMetrikaStats>
    suspend fun getGeneralData(project_id: Int, stats_period: Pair<String, String>): List<GeneralStats>
    suspend fun checkAccountYandexDirect(account: String, project_id: Int): Boolean
    suspend fun checkCounterYandexMetrika(counter: String, project_id: Int): Boolean
    suspend fun getFirstUpdateDateYandexDirect(account: String, project_id: Int): String
    suspend fun getFirstUpdateDateYandexMetrika(counter: String, project_id: Int): String
    suspend fun getFirstUpdateDateGeneral(project_id: Int): String
    suspend fun getLastUpdateDateYandexDirect(account: String, project_id: Int): String
    suspend fun getLastUpdateDateYandexMetrika(counter: String, project_id: Int): String
    suspend fun getLastUpdateDateGeneral(project_id: Int): String
    suspend fun clearStats(project_id: Int)
}