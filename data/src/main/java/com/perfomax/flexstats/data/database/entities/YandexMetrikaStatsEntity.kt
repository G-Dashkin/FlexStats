package com.perfomax.flexstats.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = YandexMetrikaStatsEntity.TABLE_NAME)
data class YandexMetrikaStatsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "counter")
    val counter: String,
    @ColumnInfo(name = "transactions")
    val transactions: Int,
    @ColumnInfo(name = "revenue")
    val revenue: Long,
    @ColumnInfo(name = "project_id")
    val project_id: Int
) {
    companion object {
        const val TABLE_NAME = "yandex_metrika"
        const val ID = "id"
        const val DATE = "date"
        const val COUNTER = "counter"
        const val TRANSACTIONS = "transactions"
        const val REVENUE = "revenue"
        const val PROJECT_ID = "project_id"
    }
}