package com.perfomax.flexstats.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = AccountEntity.TABLE_NAME)
data class AccountEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "account_name")
    val name: String,
    @ColumnInfo(name = "account_token")
    val token: String,
    @ColumnInfo(name = "account_type")
    val type: String,
    @ColumnInfo(name = "metrika_counter")
    val metrikaCounter: String,
    @ColumnInfo(name = "project_id")
    val projectId: Int
) {
    companion object {
        const val TABLE_NAME = "accounts"
        const val ID = "id"
        const val ACCOUNT_NAME = "project_name"
        const val ACCOUNT_TOKEN = "account_token"
        const val ACCOUNT_TYPE = "account_type"
        const val METRIKA_COUNTER = "metrika_counter"
        const val PROJECT_ID = "project_id"
    }
}