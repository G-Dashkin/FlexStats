package com.perfomax.flexstats.accounts.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.accounts.domain.CreateAccountUseCase
import com.perfomax.flexstats.accounts.domain.DeleteAccountUseCase
import com.perfomax.flexstats.accounts.domain.GeAccountsByProjectUseCase
import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.Project
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AccountsScreen {
    data object AddNewAccount : AccountsScreen()
    data class DeleteAccount(val accountId: Int, val accountName: String) : AccountsScreen()
    data object Nothing : AccountsScreen()
}
class AccountsViewModel(
    private val createAccountUseCase: CreateAccountUseCase,
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
            val student = geAccountsByProjectUseCase.execute()
            _accountsList.postValue(student)
        }
    }

    fun showAddAccountDialog(){
        _accountsScreen.value = AccountsScreen.AddNewAccount
    }

    fun addNewAccount(accountName: String, accountPassword: String,){
        viewModelScope.launch {

            // Здесь будет передача логина и пароля аккауна яндекс директ и дальшейшая логика
            // авторизации
            val login = accountName
            val password = accountPassword

            createAccountUseCase.execute(Account(name = accountName, token = "здесь должен ьыть токен"))
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
            deleteAccountUseCase = deleteAccountUseCase,
            geAccountsByProjectUseCase = geAccountsByProjectUseCase
        ) as T
    }
}