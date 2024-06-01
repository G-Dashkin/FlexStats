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

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME} ORDER BY ${UserEntity.ID} ASC")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("UPDATE ${UserEntity.TABLE_NAME} SET ${UserEntity.USER_IS_LOGIN} = " +
           "CASE WHEN ${UserEntity.ID} = :userId THEN 1 ELSE 0 END")
    suspend fun setAuthUserBase(userId: String)

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.USER_IS_LOGIN} = 1")
    suspend fun getAuthUserBase(): UserEntity

    @Query("UPDATE ${UserEntity.TABLE_NAME} SET ${UserEntity.USER_IS_LOGIN} = 0")
    suspend fun logout()
}