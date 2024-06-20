package com.perfomax.flexstats.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfomax.flexstats.data.database.entities.ProjectEntity
import com.perfomax.flexstats.data.database.entities.YandexDirectStatsEntity

@Dao
interface YandexDirectStatsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: YandexDirectStatsEntity)

    @Query("SELECT * FROM ${YandexDirectStatsEntity.TABLE_NAME} ")
    fun getData(): List<YandexDirectStatsEntity>
}