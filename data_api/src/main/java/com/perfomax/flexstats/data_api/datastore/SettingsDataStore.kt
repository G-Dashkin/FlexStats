package com.perfomax.flexstats.data_api.datastore

import com.perfomax.flexstats.models.User

interface SettingsDataStore {
    suspend fun setUpdateBackground()
    suspend fun setUpdateTime()
}