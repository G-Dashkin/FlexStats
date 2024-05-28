package com.perfomax.flexstats.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.perfomax.flexstats.core.utils.parsStringToUser
import com.perfomax.flexstats.core.utils.parsUserToString
import com.perfomax.flexstats.models.User
import com.perfomax.flexstats.data_api.datastore.AuthDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(AuthDataStoreImpl.AUTH_DATASTORE)

class AuthDataStoreImpl @Inject constructor(
    private val context: Context
): AuthDataStore {

    companion object {
        const val AUTH_DATASTORE = "AUTH_DATASTORE"
        val USER = stringPreferencesKey("USER")
        val AUTH = booleanPreferencesKey("AUTH")
    }

    override suspend fun getAuthUser(): User {
        val preference = context.dataStore.data.first()
        return preference[USER]!!.parsStringToUser()
    }

    override suspend fun setAuthUser(authUser: User) {
        context.dataStore.edit { preference ->
            preference[USER] = authUser.parsUserToString()
        }
    }

    override suspend fun setAuth() {
        context.dataStore.edit { preference ->
            preference[AUTH] = true
        }
    }

    override suspend fun getAuth(): Boolean {
        val preference = context.dataStore.data.first()
        return preference[AUTH]?:false
    }

    override suspend fun logout() {
        context.dataStore.edit { preference ->
            preference[AUTH] = false
        }
    }
}