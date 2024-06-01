package com.perfomax.flexstats.data.database.entities

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = ProjectEntity.TABLE_NAME)
data class ProjectEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "project_name")
    val project: String,
    @ColumnInfo(name = "project_is_selected")
    val isSelected: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int
) {
    companion object {
        const val TABLE_NAME = "projects"
        const val ID = "id"
        const val PROJECT_NAME = "project_name"
        const val SELECTED_PROJECT = "project_is_selected"
        const val USER_ID = "user_id"
    }
}