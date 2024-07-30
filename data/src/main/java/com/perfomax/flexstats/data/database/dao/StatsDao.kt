package com.perfomax.flexstats.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfomax.flexstats.data.database.entities.GeneralStatsEntity
import com.perfomax.flexstats.data.database.entities.YandexDirectStatsEntity
import com.perfomax.flexstats.data.database.entities.YandexMetrikaStatsEntity

@Dao
interface StatsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertYandexDirectData(data: YandexDirectStatsEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertYandexMetrikaData(data: YandexMetrikaStatsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneralData(data: GeneralStatsEntity)

    @Query("DELETE FROM ${YandexDirectStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexDirectStatsEntity.DATE} = :date " +
           "AND ${YandexDirectStatsEntity.PROJECT_ID} = :projectId " +
           "AND ${YandexDirectStatsEntity.ACCOUNT} = :account")
    suspend fun removeYandexDirectData(date: String, projectId: Int, account: String)

    @Query("DELETE FROM ${YandexMetrikaStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexMetrikaStatsEntity.DATE} = :date " +
           "AND ${YandexMetrikaStatsEntity.PROJECT_ID} = :projectId " +
           "AND ${YandexMetrikaStatsEntity.COUNTER} = :counter")
    suspend fun removeYandexMetrikaData(date: String, projectId: Int, counter: String)

    @Query("DELETE FROM ${GeneralStatsEntity.TABLE_NAME} " +
           "WHERE ${GeneralStatsEntity.DATE} = :date " +
           "AND ${GeneralStatsEntity.PROJECT_ID} = :projectId")
    suspend fun removeGeneralData(date: String, projectId: Int)

    @Query("SELECT * FROM ${YandexDirectStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexDirectStatsEntity.ACCOUNT} = :account " +
           "AND ${YandexDirectStatsEntity.PROJECT_ID} = :projectId " +
           "ORDER BY ${YandexDirectStatsEntity.DATE} DESC")
    suspend fun getLastDateYandexDirectByAccount(account: String, projectId: Int): List<YandexDirectStatsEntity>

    @Query("SELECT * FROM ${YandexDirectStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexDirectStatsEntity.PROJECT_ID} = :projectId " +
           "ORDER BY ${YandexDirectStatsEntity.DATE} DESC")
    suspend fun getLastDateYandexDirectByAccount(projectId: Int): List<YandexDirectStatsEntity>

    @Query("SELECT * FROM ${YandexDirectStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexDirectStatsEntity.ACCOUNT} = :account " +
           "AND ${YandexDirectStatsEntity.PROJECT_ID} = :projectId " +
           "ORDER BY ${YandexDirectStatsEntity.DATE} ASC")
    suspend fun getFirstDateYandexDirectByAccount(account: String, projectId: Int): List<YandexDirectStatsEntity>

    @Query("SELECT * FROM ${YandexDirectStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexDirectStatsEntity.PROJECT_ID} = :projectId " +
           "ORDER BY ${YandexDirectStatsEntity.DATE} ASC")
    suspend fun getFirstDateYandexDirectByAccount(projectId: Int): List<YandexDirectStatsEntity>

    @Query("SELECT * FROM ${YandexMetrikaStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexMetrikaStatsEntity.COUNTER} = :counter " +
           "AND ${YandexMetrikaStatsEntity.PROJECT_ID} = :projectId " +
           "ORDER BY ${YandexMetrikaStatsEntity.DATE} DESC")
    suspend fun getLastDateYandexMetrikaByCounter(counter: String, projectId: Int): List<YandexMetrikaStatsEntity>

    @Query("SELECT * FROM ${YandexMetrikaStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexMetrikaStatsEntity.PROJECT_ID} = :projectId " +
           "ORDER BY ${YandexMetrikaStatsEntity.DATE} DESC")
    suspend fun getLastDateYandexMetrikaByCounter(projectId: Int): List<YandexMetrikaStatsEntity>

    @Query("SELECT * FROM ${YandexMetrikaStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexMetrikaStatsEntity.COUNTER} = :counter " +
           "AND ${YandexMetrikaStatsEntity.PROJECT_ID} = :projectId " +
           "ORDER BY ${YandexMetrikaStatsEntity.DATE} ASC")
    suspend fun getFirstDateYandexMetrikaByCounter(counter: String, projectId: Int): List<YandexMetrikaStatsEntity>

    @Query("SELECT * FROM ${YandexMetrikaStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexMetrikaStatsEntity.PROJECT_ID} = :projectId " +
           "ORDER BY ${YandexMetrikaStatsEntity.DATE} ASC")
    suspend fun getFirstDateYandexMetrikaByCounter(projectId: Int): List<YandexMetrikaStatsEntity>

    @Query("SELECT * FROM ${YandexDirectStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexDirectStatsEntity.DATE} = :date " +
           "AND ${YandexDirectStatsEntity.PROJECT_ID} = :projectId")
    suspend fun getYandexDirectData(date: String, projectId: Int): List<YandexDirectStatsEntity>

    @Query("SELECT * FROM ${YandexMetrikaStatsEntity.TABLE_NAME} " +
           "WHERE ${YandexMetrikaStatsEntity.DATE} = :date " +
           "AND ${YandexMetrikaStatsEntity.PROJECT_ID} = :projectId")
    suspend fun getYandexMetrikaData(date: String, projectId: Int): List<YandexMetrikaStatsEntity>

    @Query("SELECT * FROM ${GeneralStatsEntity.TABLE_NAME} " +
           "WHERE ${GeneralStatsEntity.PROJECT_ID} = :projectId " +
           "AND ${GeneralStatsEntity.DATE} >= :firstDate " +
           "AND ${GeneralStatsEntity.DATE} <= :secondDate " +
           "ORDER BY ${GeneralStatsEntity.DATE} DESC")
    suspend fun getGeneralData(projectId: Int, firstDate: String, secondDate: String): List<GeneralStatsEntity>

    @Query("DELETE FROM ${YandexDirectStatsEntity.TABLE_NAME} " +
            "WHERE ${YandexDirectStatsEntity.PROJECT_ID} = :projectId")
    suspend fun clearYandexDirect(projectId: Int)

    @Query("DELETE FROM ${YandexMetrikaStatsEntity.TABLE_NAME} " +
            "WHERE ${YandexMetrikaStatsEntity.PROJECT_ID} = :projectId")
    suspend fun clearYandexMetrika(projectId: Int)

    @Query("DELETE FROM ${GeneralStatsEntity.TABLE_NAME} " +
            "WHERE ${GeneralStatsEntity.PROJECT_ID} = :projectId")
    suspend fun clearGeneral(projectId: Int)

}