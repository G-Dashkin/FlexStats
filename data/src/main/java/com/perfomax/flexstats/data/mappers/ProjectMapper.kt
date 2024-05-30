package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.data.database.entities.ProjectEntity
import com.perfomax.flexstats.data.database.entities.UserEntity
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.User

internal fun ProjectEntity.toDomain(): Project {
    return Project (
        id = id,
        name = project,
        isSelected = if (isSelected == 0) false else true,
        userId = userId
    )
}

internal fun Project.toDomain(): ProjectEntity {
    return ProjectEntity (
        id = id?:0,
        project = name,
        isSelected = if (isSelected == false) 0 else 1,
        userId = userId?:0
    )
}