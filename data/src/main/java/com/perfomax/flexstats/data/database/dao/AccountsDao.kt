package com.perfomax.flexstats.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfomax.flexstats.data.database.entities.AccountEntity

@Dao
interface AccountsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newAccount: AccountEntity)

    @Query("DELETE FROM ${AccountEntity.TABLE_NAME} " +
            "WHERE ${AccountEntity.TABLE_NAME}.${AccountEntity.ID} = :accountId")
    suspend fun delete(accountId: String)

    @Query("SELECT * FROM ${AccountEntity.TABLE_NAME} " +
            "WHERE ${AccountEntity.PROJECT_ID} = :projectId " +
            "ORDER BY ${AccountEntity.ID} ASC")
    suspend fun getAllAccountOfUser(projectId: String): List<AccountEntity>
}