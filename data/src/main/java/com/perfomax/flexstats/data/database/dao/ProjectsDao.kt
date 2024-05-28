package com.perfomax.flexstats.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfomax.flexstats.data.database.entities.ProjectEntity
import com.perfomax.flexstats.data.database.entities.UserEntity

@Dao
interface ProjectsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newProject: ProjectEntity)

    @Delete
    suspend fun delete(project: ProjectEntity)

    @Query("SELECT * from ${ProjectEntity.TABLE_NAME} ORDER BY ${ProjectEntity.ID} ASC")
    suspend fun getAllProjects(): List<ProjectEntity>
}