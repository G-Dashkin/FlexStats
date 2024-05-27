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

sealed class RegisterScreen {
    data object Login : RegisterScreen()
    data object Back : RegisterScreen()
    data object Nothing : RegisterScreen()
}

class RegisterViewModel(
    private val context: Context,
    private val getUsersUseCase: GetUsersUseCase,
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private val _registerScreen = MutableLiveData<RegisterScreen>()
    val registerScreen: LiveData<RegisterScreen> = _registerScreen

    fun onRegisterClicked(email: String, user: String, password: String) {
        Log.d("MyLog", "onRegisterClicked()")
        Log.d("MyLog", "email: $email, user: $user, password:$password")
        // Получаем список всех зарегистрированных пользователей
        viewModelScope.launch {
            val usersArray = getUsersUseCase.execute()
            Log.d("MyLog", usersArray.toString())
//            registerUseCase.execute(User(email = email, user = user, password = password))
            _registerScreen.value = RegisterScreen.Login
        }
    }

    fun showLoginScreen() {
        _registerScreen.value = RegisterScreen.Login
    }

    fun backClicked() {
        _registerScreen.value = RegisterScreen.Back
        _registerScreen.value = RegisterScreen.Nothing
    }
}

class RegisterViewModelFactory @Inject constructor(
    private val context: Context,
    private val getUsersUseCase: GetUsersUseCase,
    private val registerUseCase: RegisterUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return RegisterViewModel(
            context = context,
            getUsersUseCase = getUsersUseCase,
            registerUseCase = registerUseCase
        ) as T
    }
}