package com.perfomax.flexstats.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.perfomax.flexstats.data.database.entities.UserEntity

@Dao
interface AuthDao {
    @Query("SELECT * from ${UserEntity.TABLE_NAME} ORDER BY ${UserEntity.ID} ASC")
    suspend fun getAllUsers(): List<UserEntity>
}