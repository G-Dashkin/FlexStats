package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.data.database.entities.ProjectEntity
import com.perfomax.flexstats.data.database.entities.YandexDirectStatsEntity
import com.perfomax.flexstats.data.database.entities.YandexMetrikaStatsEntity
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats

internal fun YandexDirectStatsEntity.toDomain(): YandexDirectStats {
    return YandexDirectStats (
        date = date,
        account = account,
        impressions = impressions,
        cost = cost,
        clicks = clicks,
        project_id = project_id
    )
}

internal fun YandexDirectStats.toDomain(): YandexDirectStatsEntity {
    return YandexDirectStatsEntity (
        id = id?:0,
        date = date?:"",
        account = account?:"",
        impressions = impressions?:0,
        cost = cost?:0.0,
        clicks = clicks?:0,
        project_id = project_id?:0
    )
}

internal fun YandexMetrikaStatsEntity.toDomain(): YandexMetrikaStats {
    return YandexMetrikaStats (
        date = date,
        counter = counter,
        transactions = transactions,
        revenue = revenue,
        project_id = project_id
    )
}

internal fun YandexMetrikaStats.toDomain(): YandexMetrikaStatsEntity {
    return YandexMetrikaStatsEntity (
        id = id?:0,
        date = date?:"",
        counter = counter?:"",
        transactions = transactions?:0,
        revenue = revenue?:0.0,
        project_id = project_id?:0
    )
}