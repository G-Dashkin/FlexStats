package com.perfomax.flexstats.data.storage

import android.util.Log
import com.perfomax.flexstats.data.database.dao.AuthDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.models.User
import com.perfomax.flexstats.data_api.datastore.AuthDataStore
import com.perfomax.flexstats.data_api.storage.AuthStorage
import javax.inject.Inject

class AuthStorageImpl @Inject constructor(
    private val authDao: AuthDao,
    private val authDataStore: AuthDataStore
): AuthStorage {
    override suspend fun add(newUser: User) = authDao.insert(newUser.toDomain())
    override suspend fun getAllUsers(): List<User> = authDao.getAllUsers().map { it.toDomain() }
    override suspend fun setAuth() = authDataStore.setAuth()
    override suspend fun getAuth(): Boolean = authDataStore.getAuth()
    override suspend fun logout() = authDataStore.logout()
    override suspend fun setAuthUser(authUser: User) = authDataStore.setAuthUser(authUser)
    override suspend fun getAuthUser(): User = authDataStore.getAuthUser()
}