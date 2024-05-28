package com.perfomax.flexstats.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.perfomax.flexstats.core.utils.parsStringToUser
import com.perfomax.flexstats.core.utils.parsUserToString
import com.perfomax.flexstats.models.User
import com.perfomax.flextats.data_api.datastore.AuthDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(AuthDataStoreImpl.AUTH_DATASTORE)

class AuthDataStoreImpl @Inject constructor(
    private val context: Context
): AuthDataStore {

    companion object {
        const val AUTH_DATASTORE = "AUTH_DATASTORE"
        val AUTH_USER = stringPreferencesKey("AUTH_USER")
    }

    override suspend fun setAuth(authUser: User) {
        context.dataStore.edit { preference ->
            preference[AUTH_USER] = authUser.parsUserToString()
        }
    }

    override suspend  fun getAuth(): User {
        val preference = context.dataStore.data.first()
        return preference[AUTH_USER]!!.parsStringToUser()
    }
}