package com.perfomax.flexstats.auth.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.auth.domain.usecases.GetAllUsersUseCase
import com.perfomax.flexstats.auth.domain.usecases.RegisterUseCase
import com.perfomax.flexstats.models.User
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegisterScreen {
    data object Login : RegisterScreen()
    data object Back : RegisterScreen()
    data object EmailExists : RegisterScreen()
    data object EmailNotCorrect : RegisterScreen()
    data object EmptyFields : RegisterScreen()
    data object Nothing : RegisterScreen()
}

class RegisterViewModel(
    private val context: Context,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private val _registerScreen = MutableLiveData<RegisterScreen>()
    val registerScreen: LiveData<RegisterScreen> = _registerScreen

    fun onRegisterClicked(email: String, user: String, password: String) {
        viewModelScope.launch {
            val usersArray = getAllUsersUseCase.execute()
            if (email.isEmpty() || user.isEmpty() || password.isEmpty()) {
                _registerScreen.value = RegisterScreen.EmptyFields
            } else if (!email.contains("@") || !email.contains(".")){
                _registerScreen.value = RegisterScreen.EmailNotCorrect
            } else if (usersArray.any {it.email == email}) {
                _registerScreen.value = RegisterScreen.EmailExists
            } else {
                registerUseCase.execute(User(email = email, user = user, password = password, isLogin = false))
                _registerScreen.value = RegisterScreen.Login
            }
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
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val registerUseCase: RegisterUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return RegisterViewModel(
            context = context,
            getAllUsersUseCase = getAllUsersUseCase,
            registerUseCase = registerUseCase
        ) as T
    }
}