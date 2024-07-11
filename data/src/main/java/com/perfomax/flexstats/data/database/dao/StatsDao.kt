package com.perfomax.flexstats.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfomax.flexstats.data.database.entities.GeneralStatsEntity
import com.perfomax.flexstats.data.database.entities.StatsEntity
import com.perfomax.flexstats.data.database.entities.YandexDirectStatsEntity
import com.perfomax.flexstats.data.database.entities.YandexMetrikaStatsEntity
import com.perfomax.flexstats.models.Account

@Dao
interface StatsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertYandexDirectData(data: YandexDirectStatsEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertYandexMetrikaData(data: YandexMetrikaStatsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneralData(data: GeneralStatsEntity)

    @Query("DELETE FROM ${YandexDirectStatsEntity.TABLE_NAME} " +
           "WHERE date = :date AND project_id = :projectId AND account = :account")
    suspend fun removeYandexDirectData(date: String, projectId: Int, account: String)

    @Query("DELETE FROM ${YandexMetrikaStatsEntity.TABLE_NAME} " +
           "WHERE date = :date AND project_id = :projectId AND counter = :counter")
    suspend fun removeYandexMetrikaData(date: String, projectId: Int, counter: String)

    @Query("DELETE FROM ${GeneralStatsEntity.TABLE_NAME} " +
            "WHERE date = :date AND project_id = :projectId")
    suspend fun removeGeneralData(date: String, projectId: Int)

    @Query("SELECT date FROM yandex_direct " +
            "GROUP BY date")
    suspend fun getData(): List<StatsEntity>

    @Query("SELECT * FROM yandex_direct WHERE account = :account AND project_id = :projectId " +
           "ORDER BY date DESC")
    suspend fun getDateYDByAccount(account: String, projectId: Int): List<YandexDirectStatsEntity>

    @Query("SELECT * FROM yandex_metrika WHERE counter = :counter AND project_id = :projectId " +
            "ORDER BY date DESC")
    suspend fun getDateYMByCounter(counter: String, projectId: Int): List<YandexMetrikaStatsEntity>

    @Query("SELECT * FROM yandex_direct WHERE date = :date AND project_id = :projectId")
    suspend fun getYandexDirectData(date: String, projectId: Int): List<YandexDirectStatsEntity>

    @Query("SELECT * FROM yandex_metrika WHERE date = :date AND project_id = :projectId")
    suspend fun getYandexMetrikaData(date: String, projectId: Int): List<YandexMetrikaStatsEntity>

    @Query("SELECT * FROM general_stats")
    suspend fun getGeneralData(): List<GeneralStatsEntity>
}