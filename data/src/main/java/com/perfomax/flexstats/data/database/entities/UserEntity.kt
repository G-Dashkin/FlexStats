package com.perfomax.flexstats.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserEntity.TABLE_NAME)
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "user_name")
    val user: String,
    @ColumnInfo(name = "user_email")
    val email: String,
    @ColumnInfo(name = "user_password")
    val password: String
) {
    companion object {
        const val TABLE_NAME = "users"
        const val ID = "id"
        const val USER = "user"
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }
}