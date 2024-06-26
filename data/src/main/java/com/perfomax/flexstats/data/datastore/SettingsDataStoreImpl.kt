package com.perfomax.flexstats.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.perfomax.flexstats.data_api.datastore.SettingsDataStore
import javax.inject.Inject


private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(SettingsDataStoreImpl.SETTINGS_DATASTORE)

class SettingsDataStoreImpl @Inject constructor(
    private val context: Context
): SettingsDataStore {

    companion object {
        const val SETTINGS_DATASTORE = "SETTINGS_DATASTORE"
        val USER = stringPreferencesKey("USER")
        val AUTH = booleanPreferencesKey("AUTH")
    }

    override suspend fun setUpdateBackground() {
        TODO("Not yet implemented")
    }

    override suspend fun setUpdateTime() {
        TODO("Not yet implemented")
    }

}