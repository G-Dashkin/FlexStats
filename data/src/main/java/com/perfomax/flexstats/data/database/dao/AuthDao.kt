package com.perfomax.flexstats.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfomax.flexstats.data.database.entities.UserEntity

@Dao
interface AuthDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newUser: UserEntity)

    @Query("SELECT * from ${UserEntity.TABLE_NAME} ORDER BY ${UserEntity.ID} ASC")
    suspend fun getAllUsers(): List<UserEntity>
}