package com.perfomax.flexstats.data_api.datastore

interface SettingsDataStore {
    suspend fun setUpdateBackground()
    suspend fun setUpdateTime()
}