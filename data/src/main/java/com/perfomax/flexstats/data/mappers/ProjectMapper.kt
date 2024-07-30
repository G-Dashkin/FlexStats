package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.data.database.entities.ProjectEntity
import com.perfomax.flexstats.models.Project

internal fun ProjectEntity.toDomain(): Project {
    return Project (
        id = id,
        name = project,
        isSelected = isSelected != 0,
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