package com.perfomax.flexstats.home.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.home.domain.usecases.ClearStatsUseCase
import com.perfomax.flexstats.home.domain.usecases.GetGeneralUseCase
import com.perfomax.flexstats.home.domain.usecases.LoadStatsUseCase
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

sealed class HomeScreen {
    data object ShowTitle : HomeScreen()
    data object HideTitle : HomeScreen()
    data object ShowProgressIndicator : HomeScreen()
    data object HideProgressIndicator : HomeScreen()
    data object ShowDatePicker : HomeScreen()
}

class HomeViewModel(
    private val loadStatsUseCase: LoadStatsUseCase,
    private val getGeneralUseCase: GetGeneralUseCase,
    private val clearStatsUseCase: ClearStatsUseCase
): ViewModel() {

    private var _statsList = MutableLiveData<List<GeneralStats>>()
    val statsList: LiveData<List<GeneralStats>> = _statsList

    private var _selectedStatsPeriod = MutableLiveData(Pair(
            defaultStatsPeriod().get("standardDate")!!.first,
            defaultStatsPeriod().get("standardDate")!!.second
    ))
    val selectedStatsPeriod: LiveData<Pair<String, String>> = _selectedStatsPeriod

    private val _homeScreen = MutableLiveData<HomeScreen>()
    val homeScreen: LiveData<HomeScreen> = _homeScreen

    init {
        loadGeneralStatsList()
    }

    private fun loadGeneralStatsList() {
        viewModelScope.launch {
            val stats = getGeneralUseCase.execute(
                statsPeriod = Pair(
                    first = _selectedStatsPeriod.value!!.first,
                    second = _selectedStatsPeriod.value!!.second
                )
            )
            _statsList.postValue(stats)
            delay(100)
            if (_statsList.value.isNullOrEmpty()) _homeScreen.value = HomeScreen.HideTitle
            else _homeScreen.value = HomeScreen.ShowTitle
        }
    }


    fun updateStats() {
        viewModelScope.launch {
            _homeScreen.value = HomeScreen.ShowProgressIndicator
            loadStatsUseCase.execute()
            loadGeneralStatsList()
            _homeScreen.value = HomeScreen.HideProgressIndicator
        }
    }

    fun clearStats() {
        viewModelScope.launch {
            clearStatsUseCase.execute()
            loadGeneralStatsList()
        }
    }

    fun selectStatsPeriod(firstDate: String, secondDate: String) {
        _selectedStatsPeriod.postValue(Pair(firstDate, secondDate))
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

    fun showDatePiker() {
        _homeScreen.value = HomeScreen.ShowDatePicker
    }

    private fun defaultStatsPeriod(): Map<String, Pair<String, String>>{

        val thirtyDays = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        thirtyDays.add(Calendar.DAY_OF_YEAR, -30)
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        val statsPeriodMap = mutableMapOf<String, Pair<String, String>>()
        statsPeriodMap.put("millisecondsDate", Pair(
            thirtyDays.timeInMillis.toString(),
            yesterday.timeInMillis.toString()
            )
        )
        statsPeriodMap.put("standardDate", Pair(
            convertTimeToDate(thirtyDays.timeInMillis),
            convertTimeToDate(yesterday.timeInMillis)
            )
        )
        return statsPeriodMap
    }


    fun convertTimeToDate(time:Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(utc.time)
    }

}

class HomeViewModelFactory @Inject constructor(
    private val loadStatsUseCase: LoadStatsUseCase,
    private val getGeneralUseCase: GetGeneralUseCase,
    private val clearStatsUseCase: ClearStatsUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return HomeViewModel(
            loadStatsUseCase = loadStatsUseCase,
            getGeneralUseCase = getGeneralUseCase,
            clearStatsUseCase = clearStatsUseCase
        ) as T
    }
}