package com.perfomax.flexstats.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.perfomax.flextats.data_api.datastore.AuthDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(AuthDataStoreImpl.AUTH_DATASTORE)

class AuthDataStoreImpl @Inject constructor(
    private val context: Context
): AuthDataStore {

    companion object {
        const val AUTH_DATASTORE = "AUTH_DATASTORE"
        val USERS_LIST = stringPreferencesKey("USERS_LIST")
        val AUTH_USER = stringPreferencesKey("AUTH_USER")
    }

    override suspend fun setUser(user: String) {
        context.dataStore.edit { preferences ->
            preferences[USERS_LIST] = user
        }
    }

    override suspend fun getUsers(): String {
        val preference = context.dataStore.data.first()
        return preference[USERS_LIST]?:""
    }

    override suspend fun setAuth(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_USER] = userName
        }
    }

    override suspend fun getAuth(): String {
        val preference = context.dataStore.data.first()
        return preference[AUTH_USER]?:""
    }
}