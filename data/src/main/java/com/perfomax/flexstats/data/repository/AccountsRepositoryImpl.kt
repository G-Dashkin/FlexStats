package com.perfomax.flexstats.data.repository

import android.util.Log
import com.perfomax.flexstats.data_api.network.YandexAccessTokenNetwork
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.storage.AccountsStorage
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.data_api.storage.ProjectsStorage
import com.perfomax.flexstats.models.Account
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val accountsStorage: AccountsStorage,
    private val projectsStorage: ProjectsStorage,
    private val authStorage: AuthStorage,
    private val yandexAccessTokenNetwork: YandexAccessTokenNetwork,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): AccountsRepository {

    override suspend fun createAccount(account: Account) {
        val authedUserId = authStorage.getAuthUser().id
        val selectedProject = projectsStorage.getSelectedProject(userId = authedUserId?:0)
        accountsStorage.add(
            Account(
                name = account.name,
                projectId = selectedProject.id,
                accountToken = account.accountToken,
                accountType = account.accountType,
                metrikaCounter = account.metrikaCounter
            )
        )
    }

    override suspend fun createYandexToken(tokenCode: String): String {
        return yandexAccessTokenNetwork.getToken(tokenCode = tokenCode)
    }

    override suspend fun deleteAccount(accountId: Int) {
        accountsStorage.delete(accountId = accountId)
    }

    override suspend fun getAllAccountsByUser(): List<Account> {
        val authedUserId = authStorage.getAuthUser().id
        val selectedProject = projectsStorage.getSelectedProject(userId = authedUserId?:0)
        return accountsStorage.getAllAccountsOfUser(projectId = selectedProject.id?:0)
    }

    override suspend fun checkMetrikaCounter(account: Account): Boolean = withContext(dispatcher) {
        yandexAccessTokenNetwork.chekMetrikaCounterExists(account)
    }
}