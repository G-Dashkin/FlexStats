package com.perfomax.flexstats.start.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.auth.domain.usecases.GetAuthUseCase
import com.perfomax.flexstats.auth.presentation.LoginScreen
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
            Log.d("MyLog", "StartViewModel")
            Log.d("MyLog", getAuthUseCase.execute().toString())
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