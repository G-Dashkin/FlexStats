package com.perfomax.flexstats.auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.auth.domain.usecases.GetAllUsersUseCase
import com.perfomax.flexstats.auth.domain.usecases.SetAuthUseCase
import com.perfomax.flexstats.auth.domain.usecases.SetAuthUserUseCase
import com.perfomax.flexstats.models.User
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginScreen {
    data object Login : LoginScreen()
    data object Register : LoginScreen()
    data object Reset : LoginScreen()
    data object EmailNotExists : LoginScreen()
    data object PasswordNotCorrect : LoginScreen()
    data object EmptyFields : LoginScreen()
    data object Back : LoginScreen()
    data object Nothing : LoginScreen()
}
class LoginViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val setAuthUseCase: SetAuthUseCase,
    private val setAuthUserUseCase: SetAuthUserUseCase
): ViewModel() {

    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private val _loginScreen = MutableLiveData<LoginScreen>()
    val loginScreen: LiveData<LoginScreen> = _loginScreen

    fun onLoginClicked(email: String, password: String) {
        viewModelScope.launch {

            val usersArray = getAllUsersUseCase.execute()
            val user = usersArray.find { it.email == email }

            if (email.isEmpty() || email.isEmpty()) _loginScreen.value = LoginScreen.EmptyFields
            else if (user == null) _loginScreen.value = LoginScreen.EmailNotExists
            else if (user.password != password) _loginScreen.value = LoginScreen.PasswordNotCorrect
            else {
                setAuthUseCase.execute()
                setAuthUserUseCase.execute(user)
                _loginScreen.value = LoginScreen.Login
            }
        }
    }

    fun toRegisterClicked() {
        _loginScreen.value = LoginScreen.Register
    }

    fun toResetClicked() {
        _loginScreen.value = LoginScreen.Reset
    }

    fun backClicked() {
        _loginScreen.value = LoginScreen.Back
        _loginScreen.value = LoginScreen.Nothing
    }

}

class LoginViewModelFactory @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val setAuthUseCase: SetAuthUseCase,
    private val setAuthUserUseCase: SetAuthUserUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return LoginViewModel(
            getAllUsersUseCase = getAllUsersUseCase,
            setAuthUseCase = setAuthUseCase,
            setAuthUserUseCase = setAuthUserUseCase
        ) as T
    }
}