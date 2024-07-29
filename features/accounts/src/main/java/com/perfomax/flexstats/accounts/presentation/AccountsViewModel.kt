package com.perfomax.flexstats.accounts.presentation

import android.util.Log
import android.webkit.CookieManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.accounts.domain.CheckMetrikaCounterUseCase
import com.perfomax.flexstats.accounts.domain.CreateAccountUseCase
import com.perfomax.flexstats.accounts.domain.CreateTokenUseCase
import com.perfomax.flexstats.accounts.domain.DeleteAccountUseCase
import com.perfomax.flexstats.accounts.domain.GetAccountsByProjectUseCase
import com.perfomax.flexstats.accounts.domain.GetSelectedProjectUseCase
import com.perfomax.flexstats.core.contracts.YANDEX_METRIKA
import com.perfomax.flexstats.models.Account
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AccountsScreen {
    data object AddNewAccount : AccountsScreen()
    data class DeleteAccount(val accountId: Int, val accountName: String) : AccountsScreen()
    data object ProjectNotExists : AccountsScreen()
    data object MetrikaCounterNotExists : AccountsScreen()
}
class AccountsViewModel(
    private val createAccountUseCase: CreateAccountUseCase,
    private val createTokenUseCase: CreateTokenUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getAccountsByProjectUseCase: GetAccountsByProjectUseCase,
    private val getSelectedProjectUseCase: GetSelectedProjectUseCase,
    private val checkMetrikaCounterUseCase: CheckMetrikaCounterUseCase
): ViewModel()  {

    private var _accountsList = MutableLiveData<List<Account>>()
    val accountsList: LiveData<List<Account>> = _accountsList

    private var _accountsScreen = MutableLiveData<AccountsScreen>()
    val accountsScreen: LiveData<AccountsScreen> = _accountsScreen

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val accounts = getAccountsByProjectUseCase.execute()
            _accountsList.value = accounts
        }
    }

    fun showAddAccountDialog(){
        CookieManager.getInstance().removeAllCookies(null)
        viewModelScope.launch {
            if (getSelectedProjectUseCase.execute().id == 0 ) {
                _accountsScreen.value = AccountsScreen.ProjectNotExists
            } else { _accountsScreen.value = AccountsScreen.AddNewAccount }
        }
    }

    fun addNewAccount(accountName: String, metrikaCounter: String, tokenCode: String, accountType: String){
        viewModelScope.launch {
            val accountToken = createTokenUseCase.execute(tokenCode)
            val newAccount = Account(
                name = accountName,
                accountToken = accountToken,
                accountType = accountType,
                metrikaCounter = metrikaCounter
            )
            if (accountType == YANDEX_METRIKA) {
                val isMetrikaCounterExists = checkMetrikaCounterUseCase.execute(newAccount)
                if (isMetrikaCounterExists) createAccountUseCase.execute(newAccount)
                else _accountsScreen.value = AccountsScreen.MetrikaCounterNotExists
            }
            else createAccountUseCase.execute(newAccount)
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
    private val getAccountsByProjectUseCase: GetAccountsByProjectUseCase,
    private val getSelectedProjectUseCase: GetSelectedProjectUseCase,
    private val checkMetrikaCounterUseCase: CheckMetrikaCounterUseCase
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
            getAccountsByProjectUseCase = getAccountsByProjectUseCase,
            getSelectedProjectUseCase = getSelectedProjectUseCase,
            checkMetrikaCounterUseCase = checkMetrikaCounterUseCase
        ) as T
    }
}