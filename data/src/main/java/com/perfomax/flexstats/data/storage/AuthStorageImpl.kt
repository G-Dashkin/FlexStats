package com.perfomax.flexstats.data.storage

import com.perfomax.flexstats.core.contracts.EMPTY
import com.perfomax.flexstats.data.database.dao.AuthDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.models.User
import com.perfomax.flexstats.data_api.storage.AuthStorage
import javax.inject.Inject

class AuthStorageImpl @Inject constructor(
    private val authDao: AuthDao
): AuthStorage {
    override suspend fun add(newUser: User) = authDao.insert(newUser.toDomain())
    override suspend fun getAllUsers(): List<User> = authDao.getAllUsers().map { it.toDomain() }
    override suspend fun setAuth() { authDao.getAuthUserBase() }
    override suspend fun getAuth(): Boolean = authDao.getAuthUserBase() != null

    override suspend fun logout() = authDao.logout()
    override suspend fun setAuthUser(authUser: User) {
        authDao.setAuthUserBase(authUser.id.toString())
    }
    override suspend fun getAuthUser(): User {
        return if (authDao.getAuthUserBase() != null) authDao.getAuthUserBase().toDomain()
        else User(user = EMPTY, email = EMPTY, password = EMPTY, isLogin = false)
    }
}