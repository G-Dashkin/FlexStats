package com.perfomax.flexstats.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.home.domain.usecases.GetGeneralUseCase
import com.perfomax.flexstats.home.domain.usecases.GetYandexDirectUseCase
import com.perfomax.flexstats.home.domain.usecases.GetYandexMetrikaUseCase
import com.perfomax.flexstats.home.domain.usecases.LoadStatsUseCase
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    private val loadStatsUseCase: LoadStatsUseCase,
    private val getYandexDirectUseCase: GetYandexDirectUseCase,
    private val getYandexMetrikaUseCase: GetYandexMetrikaUseCase,
    private val getGeneralUseCase: GetGeneralUseCase
): ViewModel() {

    private val _statsList = MutableLiveData<List<GeneralStats>>()
    val statsList: LiveData<List<GeneralStats>> = _statsList

    private val _homeScreen = MutableLiveData<YandexDirectStats>()
    val homeScreen: LiveData<YandexDirectStats> = _homeScreen

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val stats = getGeneralUseCase.execute()
            _statsList.postValue(stats)
        }
    }


    fun loadStats() {
        viewModelScope.launch {
            loadStatsUseCase.execute()
        }
    }

    fun getYandexDirect() {
        viewModelScope.launch {
            getYandexDirectUseCase.execute()
        }
    }

    fun getYandexMetrika() {
        viewModelScope.launch {
            getYandexMetrikaUseCase.execute()
        }
    }

    fun getGeneral() {
        viewModelScope.launch {
            getGeneralUseCase.execute()
        }
    }

}

class HomeViewModelFactory @Inject constructor(
    private val loadStatsUseCase: LoadStatsUseCase,
    private val getYandexDirectUseCase: GetYandexDirectUseCase,
    private val getYandexMetrikaUseCase: GetYandexMetrikaUseCase,
    private val getGeneralUseCase: GetGeneralUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return HomeViewModel(
            loadStatsUseCase = loadStatsUseCase,
            getYandexDirectUseCase = getYandexDirectUseCase,
            getYandexMetrikaUseCase = getYandexMetrikaUseCase,
            getGeneralUseCase = getGeneralUseCase
        ) as T
    }
}