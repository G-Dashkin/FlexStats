package com.perfomax.flexstats.auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.models.User
import javax.inject.Inject

sealed class ResetScreen {
    data object Login : ResetScreen()
    data object Back : ResetScreen()
}

class ResetViewModel: ViewModel() {

    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private val _resetScreen = MutableLiveData<ResetScreen>()
    val resetScreen: LiveData<ResetScreen> = _resetScreen

    fun showLoginScreen() {
        _resetScreen.value = ResetScreen.Login
    }

}

class ResetViewModelFactory @Inject constructor():  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return ResetViewModel() as T
    }
}