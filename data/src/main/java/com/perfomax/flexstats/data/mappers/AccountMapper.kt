package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.data.database.entities.AccountEntity
import com.perfomax.flexstats.models.Account

internal fun AccountEntity.toDomain(): Account {
    return Account (
        id = id,
        name = name,
        accountToken = token,
        projectId = projectId
    )
}

internal fun Account.toDomain(): AccountEntity {
    return AccountEntity (
        id = id?:0,
        name = name,
        token = accountToken?:"",
        projectId = projectId?:0
    )
}

//internal fun ProjectEntity.toDomain(): Project {
//    return Project (
//        id = id,
//        name = project,
//        isSelected = isSelected != 0,
//        userId = userId
//    )
//}
//
//internal fun Project.toDomain(): ProjectEntity {
//    return ProjectEntity (
//        id = id?:0,
//        project = name,
//        isSelected = if (isSelected == false) 0 else 1,
//        userId = userId?:0
//    )
//}