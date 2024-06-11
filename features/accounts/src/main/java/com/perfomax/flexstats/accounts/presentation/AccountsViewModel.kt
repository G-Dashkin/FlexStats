package com.perfomax.flexstats.accounts.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.accounts.domain.CreateAccountUseCase
import com.perfomax.flexstats.accounts.domain.CreateTokenUseCase
import com.perfomax.flexstats.accounts.domain.DeleteAccountUseCase
import com.perfomax.flexstats.accounts.domain.GeAccountsByProjectUseCase
import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.Project
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AccountsScreen {
    data object AddNewAccount : AccountsScreen()
    data class DeleteAccount(val accountId: Int, val accountName: String) : AccountsScreen()
    data object Nothing : AccountsScreen()
}
class AccountsViewModel(
    private val createAccountUseCase: CreateAccountUseCase,
    private val createTokenUseCase: CreateTokenUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val geAccountsByProjectUseCase: GeAccountsByProjectUseCase
): ViewModel()  {

    private val _accountsList = MutableLiveData<List<Account>>()
    val accountsList: LiveData<List<Account>> = _accountsList

    private val _accountsScreen = MutableLiveData<AccountsScreen>()
    val accountsScreen: LiveData<AccountsScreen> = _accountsScreen

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val accounts = geAccountsByProjectUseCase.execute()
            Log.d("MyLog","load() in ViewModel")
            Log.d("MyLog","accounts:")
            accounts.forEach { Log.d("MyLog", it.toString() ) }
            Log.d("MyLog","---------------------------------------------------------------")
            _accountsList.postValue(accounts)
        }
    }

    fun showAddAccountDialog(){
        _accountsScreen.value = AccountsScreen.AddNewAccount
    }

    fun addNewAccount(accountName: String, tokenCode: String, accountType: String, metrikaCounter: String){
        viewModelScope.launch {
            val accountToken = createTokenUseCase.execute(tokenCode)
            createAccountUseCase.execute(
                Account(
                    name = accountName,
                    accountToken = accountToken,
                    accountType = accountType,
                    metrikaCounter = metrikaCounter
                )
            )
            load()
        }
    }

    fun showDeleteAccountDialog(accountId: Int, accountName: String){
        _accountsScreen.value = AccountsScreen.DeleteAccount(
            accountId = accountId,
            accountName = accountName
        )
    }

    fun deleteAccountClicked(projectId: Int){
        viewModelScope.launch {
            deleteAccountUseCase.execute(projectId)
            load()
        }
    }


}

class AccountsViewModelFactory @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val createTokenUseCase: CreateTokenUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val geAccountsByProjectUseCase: GeAccountsByProjectUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return AccountsViewModel(
            createAccountUseCase = createAccountUseCase,
            createTokenUseCase = createTokenUseCase,
            deleteAccountUseCase = deleteAccountUseCase,
            geAccountsByProjectUseCase = geAccountsByProjectUseCase
        ) as T
    }
}