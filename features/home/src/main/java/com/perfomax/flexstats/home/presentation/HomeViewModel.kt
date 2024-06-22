package com.perfomax.flexstats.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.home.domain.usecases.GetStatsUseCase
import com.perfomax.flexstats.home.domain.usecases.LoadStatsUseCase
import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.YandexDirectStats
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    private val loadStatsUseCase: LoadStatsUseCase,
    private val getStatsUseCase: GetStatsUseCase
): ViewModel() {

    private val _statsList = MutableLiveData<List<YandexDirectStats>>()
    val statsList: LiveData<List<YandexDirectStats>> = _statsList

    private val _homeScreen = MutableLiveData<YandexDirectStats>()
    val homeScreen: LiveData<YandexDirectStats> = _homeScreen

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val stats = getStatsUseCase.execute()
            _statsList.postValue(stats)
        }
    }


    fun loadStats() {
        viewModelScope.launch {
            loadStatsUseCase.execute()
        }
    }

    fun getStats() {
        viewModelScope.launch {
            val stats = getStatsUseCase.execute()
            _statsList.postValue(stats)
        }
    }


}

class HomeViewModelFactory @Inject constructor(
    private val loadStatsUseCase: LoadStatsUseCase,
    private val getStatsUseCase: GetStatsUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return HomeViewModel(
            loadStatsUseCase = loadStatsUseCase,
            getStatsUseCase = getStatsUseCase
        ) as T
    }
}