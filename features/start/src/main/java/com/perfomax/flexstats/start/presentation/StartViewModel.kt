package com.perfomax.flexstats.start.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.start.domain.usecases.GetAuthUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class StartScreen {
    data object Login : StartScreen()
    data object Home : StartScreen()
}

class StartViewModel(
    private val getAuthUseCase: GetAuthUseCase
): ViewModel() {

    private val _startScreen = MutableLiveData<StartScreen>()
    val startScreen: LiveData<StartScreen> = _startScreen

    init {
        viewModelScope.launch {
            val isUserLogin = getAuthUseCase.execute()
            if (isUserLogin) _startScreen.value = StartScreen.Home
            else _startScreen.value = StartScreen.Login
        }
    }
}

class StartViewModelFactory @Inject constructor(
    private val getAuthUseCase: GetAuthUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return StartViewModel(
            getAuthUseCase = getAuthUseCase
        ) as T
    }
}