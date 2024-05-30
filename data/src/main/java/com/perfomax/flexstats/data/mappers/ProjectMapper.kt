package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.data.database.entities.ProjectEntity
import com.perfomax.flexstats.data.database.entities.UserEntity
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.User

internal fun ProjectEntity.toDomain(): Project {
    return Project (
        id = id,
        name = project,
        userId = userId
    )
}

internal fun Project.toDomain(): ProjectEntity {
    return ProjectEntity (
        id = id?:0,
        project = name,
        userId = userId?:0
    )
}