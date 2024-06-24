package com.perfomax.flexstats.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = YandexDirectStatsEntity.TABLE_NAME)
data class YandexDirectStatsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "account")
    val account: String,
    @ColumnInfo(name = "impressions")
    val impressions: Int,
    @ColumnInfo(name = "clicks")
    val clicks: Int,
    @ColumnInfo(name = "cost")
    val cost: Double,
    @ColumnInfo(name = "project_id")
    val project_id: Int
) {
    companion object {
        const val TABLE_NAME = "yandex_direct"
        const val ID = "id"
        const val DATE = "date"
        const val ACCOUNT = "account"
        const val IMPRESSIONS = "impressions"
        const val CLICKS = "clicks"
        const val COST = "cost"
        const val PROJECT_ID = "project_id"
    }
}