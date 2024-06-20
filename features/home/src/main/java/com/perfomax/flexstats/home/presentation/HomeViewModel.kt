package com.perfomax.flexstats.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.home.domain.usecases.GetStatsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    private val getStatsUseCase: GetStatsUseCase
): ViewModel() {


    fun loadStats() {
        viewModelScope.launch {
            getStatsUseCase.execute()
        }
    }


}

class HomeViewModelFactory @Inject constructor(
    private val getStatsUseCase: GetStatsUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return HomeViewModel(
            getStatsUseCase = getStatsUseCase
        ) as T
    }
}