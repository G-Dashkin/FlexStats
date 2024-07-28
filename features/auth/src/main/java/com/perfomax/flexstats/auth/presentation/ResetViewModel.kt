package com.perfomax.flexstats.auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.auth.domain.usecases.ResetUseCase
import com.perfomax.flexstats.models.User
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ResetScreen {
    data object Login : ResetScreen()
    data object Back : ResetScreen()
    data object ResetPasswordSuccess : ResetScreen()
    data object ResetPasswordFailed : ResetScreen()
}

class ResetViewModel(
    private val resetUseCase: ResetUseCase
): ViewModel() {

    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private val _resetScreen = MutableLiveData<ResetScreen>()
    val resetScreen: LiveData<ResetScreen> = _resetScreen

    fun resetPassword(email:String) {
        viewModelScope.launch {
            val result = resetUseCase.execute(email)
            if (result) _resetScreen.value = ResetScreen.ResetPasswordSuccess
            else _resetScreen.value = ResetScreen.ResetPasswordFailed
        }
    }

    fun showLoginScreen() {
        _resetScreen.value = ResetScreen.Login
    }
}

class ResetViewModelFactory @Inject constructor(
    private val resetUseCase: ResetUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return ResetViewModel(
            resetUseCase = resetUseCase
        ) as T
    }
}