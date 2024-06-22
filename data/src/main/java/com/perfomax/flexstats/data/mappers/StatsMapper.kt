package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.data.database.entities.ProjectEntity
import com.perfomax.flexstats.data.database.entities.YandexDirectStatsEntity
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.YandexDirectStats

internal fun YandexDirectStatsEntity.toDomain(): YandexDirectStats {
    return YandexDirectStats (
        date = date,
        account = account,
        campaign = campaign,
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
        campaign = campaign?:"",
        impressions = impressions?:0,
        cost = cost?:0.0,
        clicks = clicks?:0,
        project_id = project_id?:0
    )
}