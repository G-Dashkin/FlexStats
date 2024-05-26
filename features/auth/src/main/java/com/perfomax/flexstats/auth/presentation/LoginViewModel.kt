package com.perfomax.flexstats.auth.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.auth.domain.usecases.GetUsersUseCase
import com.perfomax.flexstats.auth.domain.usecases.RegisterUseCase
import com.perfomax.flexstats.auth.domain.usecases.SetAuthUseCase
import com.perfomax.flexstats.models.User
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginScreen {
    data object Login : LoginScreen()
    data object Register : LoginScreen()
    data object Reset : LoginScreen()
    data object Back : LoginScreen()
    data object Nothing : LoginScreen()
}
class LoginViewModel(
    private val context: Context,
    private val getUsersUseCase: GetUsersUseCase,
    private val setAuthUseCase: SetAuthUseCase
): ViewModel() {

    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private val _loginScreen = MutableLiveData<LoginScreen>()
    val loginScreen: LiveData<LoginScreen> = _loginScreen

    fun onLoginClicked(user: String, password: String) {
        Log.d("MyLog", "onLoginClicked()")
        Log.d("MyLog", "user: $user, password:$password")

        viewModelScope.launch {
            Log.d("MyLog", getUsersUseCase.execute().toString())

//            registerUseCase.execute(User(user=user, ))
        }
//        _loginScreen.value = LoginScreen.Login
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
    private val context: Context,
    private val getUsersUseCase: GetUsersUseCase,
    private val setAuthUseCase: SetAuthUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return LoginViewModel(
            context = context,
            getUsersUseCase = getUsersUseCase,
            setAuthUseCase = setAuthUseCase
        ) as T
    }
}