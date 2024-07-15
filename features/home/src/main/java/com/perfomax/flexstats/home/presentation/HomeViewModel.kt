package com.perfomax.flexstats.home.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.home.domain.usecases.GetGeneralUseCase
import com.perfomax.flexstats.home.domain.usecases.LoadStatsUseCase
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    private val loadStatsUseCase: LoadStatsUseCase,
    private val getGeneralUseCase: GetGeneralUseCase
): ViewModel() {

    private val _statsList = MutableLiveData<List<GeneralStats>>()
    val statsList: LiveData<List<GeneralStats>> = _statsList

    private val _progressIndicator = MutableLiveData(false)
    val progressIndicator: LiveData<Boolean> = _progressIndicator

    private val _homeScreen = MutableLiveData<YandexDirectStats>()
    val homeScreen: LiveData<YandexDirectStats> = _homeScreen

    val DEFAULT_FIRST_DATE = "2024-07-10"
    val DEFAULT_SECOND_DATE = "2024-07-13"

    init {
        loadGeneralStatsList()
    }

    private fun loadGeneralStatsList() {
        viewModelScope.launch {
            val stats = getGeneralUseCase.execute(
                statsPeriod = Pair(
                    first = DEFAULT_FIRST_DATE,
                    second = DEFAULT_SECOND_DATE
                )
            )
            _statsList.postValue(stats)
        }
    }


    fun updateStats() {
        viewModelScope.launch {
            _progressIndicator.value = true
            loadStatsUseCase.execute()
            loadGeneralStatsList()
            _progressIndicator.value = false
        }
    }

    fun selectStatsPeriod(firstDate: String, secondDate: String) {
        viewModelScope.launch {
            val stats = getGeneralUseCase.execute(
                statsPeriod = Pair(
                    first = firstDate,
                    second = secondDate
                )
            )
            _statsList.postValue(stats)
        }

    }

}

class HomeViewModelFactory @Inject constructor(
    private val loadStatsUseCase: LoadStatsUseCase,
    private val getGeneralUseCase: GetGeneralUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return HomeViewModel(
            loadStatsUseCase = loadStatsUseCase,
            getGeneralUseCase = getGeneralUseCase
        ) as T
    }
}