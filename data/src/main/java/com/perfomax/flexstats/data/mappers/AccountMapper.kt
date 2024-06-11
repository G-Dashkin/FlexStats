package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.data.database.entities.AccountEntity
import com.perfomax.flexstats.models.Account

internal fun AccountEntity.toDomain(): Account {
    return Account (
        id = id,
        name = name,
        accountToken = token,
        accountType = type,
        metrikaCounter = metrikaCounter,
        projectId = projectId
    )
}

internal fun Account.toDomain(): AccountEntity {
    return AccountEntity (
        id = id?:0,
        name = name,
        token = accountToken?:"",
        type = accountType?:"",
        metrikaCounter = metrikaCounter?:"",
        projectId = projectId?:0,
    )
}