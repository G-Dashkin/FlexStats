package com.perfomax.flexstats.auth.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.models.User
import javax.inject.Inject

sealed class Screen {
    data object Nothing : Screen()

    data object Login : Screen()
    data object Register : Screen()
    data object Reset : Screen()
}
class LoginViewModel: ViewModel() {

    private val _login = MutableLiveData<List<User>>()
    val login: LiveData<List<User>> = _login

    private val _screen = MutableLiveData<Screen>()
    val screen: LiveData<Screen> = _screen

    fun onLoginClicked() {
        _screen.value = Screen.Login
    }

    fun toRegisterClicked() {
        _screen.value = Screen.Register
    }

    fun toResetClicked() {
        _screen.value = Screen.Reset
    }

}

class LoginViewModelFactory @Inject constructor():  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return LoginViewModel() as T
    }
}