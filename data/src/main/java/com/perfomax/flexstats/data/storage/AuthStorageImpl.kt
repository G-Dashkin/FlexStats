package com.perfomax.flexstats.data.storage

import com.perfomax.flexstats.data.database.dao.AuthDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.models.User
import com.perfomax.flextats.data_api.storage.AuthStorage
import javax.inject.Inject

class AuthStorageImpl @Inject constructor(
    private val authDao: AuthDao
): AuthStorage {
    override suspend fun add(newUser: User) = authDao.insert(newUser.toDomain())
    override suspend fun getAllUsers(): List<User> = authDao.getAllUsers().map { it.toDomain() }
    override suspend fun setAuth(userName: String) {}
    override suspend fun getAuth(): String { return "" }
}