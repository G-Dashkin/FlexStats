package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats

interface StatsStorage {

    suspend fun addYandexDirectData(data: YandexDirectStats)
    suspend fun addYandexMetrikaData(data: YandexMetrikaStats)
    suspend fun getYD()
    suspend fun getYM()
    suspend fun getGeneral()

}