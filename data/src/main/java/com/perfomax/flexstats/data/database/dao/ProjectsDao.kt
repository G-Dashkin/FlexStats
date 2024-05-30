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

    @Query("UPDATE projects SET selected_project = " +
           "CASE WHEN projects.id = :projectId THEN 1 ELSE 0 END "  +
           "WHERE projects.user_id = :userId")
    suspend fun selectProject(userId: String, projectId: String)

    @Query("SELECT * FROM ${ProjectEntity.TABLE_NAME} " +
            "WHERE ${ProjectEntity.TABLE_NAME}.${ProjectEntity.USER_ID} = :userId " +
            "AND ${ProjectEntity.TABLE_NAME}.${ProjectEntity.SELECTED_PROJECT} = '1'"
    )
    suspend fun getSelectedProject(userId: String): ProjectEntity

    @Query("SELECT * FROM ${ProjectEntity.TABLE_NAME} ORDER BY ${ProjectEntity.ID} ASC")
    suspend fun getAllProjects(): List<ProjectEntity>
}