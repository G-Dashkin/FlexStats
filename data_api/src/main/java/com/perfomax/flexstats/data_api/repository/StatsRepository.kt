package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.Project

interface StatsRepository {
    suspend fun getStats()
}