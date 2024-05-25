package com.perfomax.flexstats.data.storage

import com.perfomax.flexstats.core.utils.addElement
import com.perfomax.flexstats.core.utils.parsToList
import com.perfomax.flextats.data_api.datastore.AuthDataStore
import com.perfomax.flextats.data_api.storage.AuthStorage
import javax.inject.Inject

class AuthStorageImpl @Inject constructor(
    private val datastore: AuthDataStore
): AuthStorage {
    override suspend fun add(user: String) {
        datastore.setUser(user = datastore.getUsers().addElement(user))
    }

    override suspend fun getAll(): List<String> {
        return datastore.getUsers().parsToList()
    }

    override suspend fun setAuth(userName: String) {
        datastore.setAuth(userName = userName)
    }

    override suspend fun getAuth(): String {
        return datastore.getAuth()
    }

}