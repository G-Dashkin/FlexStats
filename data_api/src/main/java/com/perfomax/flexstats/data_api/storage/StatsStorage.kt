package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.YandexDirectStats

interface StatsStorage {

    suspend fun addData(data: List<YandexDirectStats>)

    suspend fun getYD()
    suspend fun getYM()
    suspend fun getGeneral()

}