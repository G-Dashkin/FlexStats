package com.perfomax.flexstats.home.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.perfomax.flexstats.core.contracts.DATE_FORMAT
import com.perfomax.flexstats.home.domain.usecases.ClearStatsUseCase
import com.perfomax.flexstats.home.domain.usecases.GetAccountsByProjectUseCase
import com.perfomax.flexstats.home.domain.usecases.GetGeneralUseCase
import com.perfomax.flexstats.home.domain.usecases.UpdateStatsUseCase
import com.perfomax.flexstats.models.GeneralStats
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

private const val STANDARD_DATE = "standardDate"
private const val MILLISECONDS_DATE = "millisecondsDate"

sealed class HomeScreen {
    data object ShowTitle : HomeScreen()
    data object HideTitle : HomeScreen()
    data object StartLoadingProcess : HomeScreen()
    data object EndLoadingProcess : HomeScreen()
    data object ShowDatePicker : HomeScreen()
    data object ShowUpdatePicker : HomeScreen()
    data class ShowToast(val updateDate: String) : HomeScreen()
    data class SendUpdatedMessage(val message: String) : HomeScreen()
}

class HomeViewModel(
    private val context: Context,
    private val updateStatsUseCase: UpdateStatsUseCase,
    private val getGeneralUseCase: GetGeneralUseCase,
    private val clearStatsUseCase: ClearStatsUseCase,
    private val getAccountsByProjectUseCase: GetAccountsByProjectUseCase
): ViewModel() {

    private var _statsList = MutableLiveData<List<GeneralStats>>()
    val statsList: LiveData<List<GeneralStats>> = _statsList

    private var _selectedStatsPeriod = MutableLiveData(Pair(
            first = defaultShowStatsPeriod().get(STANDARD_DATE)!!.first,
            second = defaultShowStatsPeriod().get(STANDARD_DATE)!!.second
    ))
    val selectedStatsPeriod: LiveData<Pair<String, String>> = _selectedStatsPeriod

    private var _selectedUpdateStatsPeriod = MutableLiveData(Pair(
        first = defaultUpdateStatsPeriod().get(STANDARD_DATE)!!.first,
        second = defaultUpdateStatsPeriod().get(STANDARD_DATE)!!.second
    ))
    val selectedUpdateStatsPeriod: LiveData<Pair<String, String>> = _selectedUpdateStatsPeriod

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
            _statsList.observeForever( Observer { date ->
                if (date.isEmpty()) _homeScreen.value = HomeScreen.HideTitle
                else _homeScreen.value = HomeScreen.ShowTitle
            })
        }
    }

    fun updateStats() {
        viewModelScope.launch {
            var updateMessage: String
            val accounts = getAccountsByProjectUseCase.execute()
            if (accounts.isNotEmpty()) {
                _homeScreen.value = HomeScreen.StartLoadingProcess
                try {
                    updateStatsUseCase.execute(selectedUpdateStatsPeriod.value!!).collect { message ->
                        _homeScreen.value = HomeScreen.SendUpdatedMessage(message)
                    }
                    val firstDate = _selectedUpdateStatsPeriod.value?.first.toString()
                    val secondDate = _selectedUpdateStatsPeriod.value?.second.toString()
                    updateMessage = if (firstDate == secondDate) "${context.resources.getString(com.perfomax.ui.R.string.data_update_for)} $firstDate"
                    else "${context.resources.getString(com.perfomax.ui.R.string.data_update_for_period)}\n " +
                         "${context.resources.getString(com.perfomax.ui.R.string.from)} $firstDate " +
                         "${context.resources.getString(com.perfomax.ui.R.string.to)} $secondDate"
                } catch (e:Exception) {
                    updateMessage = context.resources.getString(com.perfomax.ui.R.string.too_long_period)
                }
                loadGeneralStatsList()
                _homeScreen.value = HomeScreen.EndLoadingProcess
                _homeScreen.value = HomeScreen.ShowToast(updateMessage)
            } else {
                updateMessage = context.resources.getString(com.perfomax.ui.R.string.accounts_not_exists)
                _homeScreen.value = HomeScreen.ShowToast(updateMessage)
            }
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

    fun selectUpdatePeriod(firstDate: String, secondDate: String) {
        _selectedUpdateStatsPeriod.postValue(Pair(firstDate, secondDate))
    }

    fun showStatsDatePiker() {
        _homeScreen.value = HomeScreen.ShowDatePicker
    }

    fun showUpdateDataPiker() {
        _homeScreen.value = HomeScreen.ShowUpdatePicker
    }

    private fun defaultShowStatsPeriod(): Map<String, Pair<String, String>>{

        val thirtyDays = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        thirtyDays.add(Calendar.DAY_OF_YEAR, -30)
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        val statsPeriodMap = mutableMapOf<String, Pair<String, String>>()
        statsPeriodMap.put(MILLISECONDS_DATE, Pair(
            thirtyDays.timeInMillis.toString(),
            yesterday.timeInMillis.toString()
            )
        )
        statsPeriodMap.put(STANDARD_DATE, Pair(
            convertTimeToDate(thirtyDays.timeInMillis),
            convertTimeToDate(yesterday.timeInMillis)
            )
        )
        return statsPeriodMap
    }

    private fun defaultUpdateStatsPeriod(): Map<String, Pair<String, String>>{

        val thirtyDays = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        thirtyDays.add(Calendar.DAY_OF_YEAR, -1)
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        val statsPeriodMap = mutableMapOf<String, Pair<String, String>>()
        statsPeriodMap.put(MILLISECONDS_DATE, Pair(
            thirtyDays.timeInMillis.toString(),
            yesterday.timeInMillis.toString()
            )
        )
        statsPeriodMap.put(STANDARD_DATE, Pair(
            convertTimeToDate(thirtyDays.timeInMillis),
            convertTimeToDate(yesterday.timeInMillis)
            )
        )
        return statsPeriodMap
    }

    fun convertTimeToDate(time:Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return format.format(utc.time)
    }

}

class HomeViewModelFactory @Inject constructor(
    private val context: Context,
    private val updateStatsUseCase: UpdateStatsUseCase,
    private val getGeneralUseCase: GetGeneralUseCase,
    private val clearStatsUseCase: ClearStatsUseCase,
    private val getAccountsByProjectUseCase: GetAccountsByProjectUseCase
):  ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        return HomeViewModel(
            context = context,
            updateStatsUseCase = updateStatsUseCase,
            getGeneralUseCase = getGeneralUseCase,
            clearStatsUseCase = clearStatsUseCase,
            getAccountsByProjectUseCase = getAccountsByProjectUseCase
        ) as T
    }
}