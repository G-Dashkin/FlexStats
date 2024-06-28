package com.perfomax.flexstats.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfomax.flexstats.data.database.entities.GeneralStatsEntity
import com.perfomax.flexstats.data.database.entities.StatsEntity
import com.perfomax.flexstats.data.database.entities.YandexDirectStatsEntity
import com.perfomax.flexstats.data.database.entities.YandexMetrikaStatsEntity

@Dao
interface StatsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertYandexDirectData(data: YandexDirectStatsEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertYandexMetrikaData(data: YandexMetrikaStatsEntity)

    @Query("DELETE FROM ${YandexDirectStatsEntity.TABLE_NAME} WHERE date = :date AND project_id = :projectId ")
    suspend fun removeYandexDirectData(date: String, projectId: Int)

    @Query("DELETE FROM ${YandexMetrikaStatsEntity.TABLE_NAME} WHERE date = :date AND project_id = :projectId ")
    suspend fun removeYandexMetrikaData(date: String, projectId: Int)

    @Query("SELECT date FROM yandex_direct " +
            "GROUP BY date")
    suspend fun getData(): List<StatsEntity>

    @Query("SELECT * FROM yandex_direct")
    suspend fun getYandexDirectData(): List<YandexDirectStatsEntity>

    @Query("SELECT * FROM yandex_metrika")
    suspend fun getYandexMetrikaData(): List<YandexMetrikaStatsEntity>

    @Query("SELECT * FROM general_stats")
    suspend fun getGeneralData(): List<GeneralStatsEntity>
}