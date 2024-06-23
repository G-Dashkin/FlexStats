package com.perfomax.flexstats.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = YandexDirectStatsEntity.TABLE_NAME)
data class StatsEntity(
    @ColumnInfo(name = "date")
    val date: String,
) {
    companion object {
        const val TABLE_NAME = "yandex_direct"
        const val ID = "id"
        const val DATE = "date"
        const val ACCOUNT = "account"
        const val CAMPAIGN = "campaign"
        const val IMPRESSIONS = "impressions"
        const val CLICKS = "clicks"
        const val COST = "cost"
        const val PROJECT_ID = "project_id"
    }
}