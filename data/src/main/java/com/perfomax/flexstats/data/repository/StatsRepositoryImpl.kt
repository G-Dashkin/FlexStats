package com.perfomax.flexstats.data.repository

import android.util.Log
import com.perfomax.flexstats.core.utils.YANDEX_DIRECT
import com.perfomax.flexstats.core.utils.YANDEX_METRIKA
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.network.YandexMetrikaStatsNetwork
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.data_api.storage.StatsStorage
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val yandexDirectStatsNetwork: YandexDirectStatsNetwork,
    private val yandexMetrikaStatsNetwork: YandexMetrikaStatsNetwork,
    private val statsStorage: StatsStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): StatsRepository {

    // Здесь добавить логику получения месяцев
    // Даты должны быть в диапазоне - от вчерашнего дня и до начала месяца
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    //Добавить логику - если данны обновлены по вчерашний день, то обновлять не нужно
    override suspend fun updateStats(): Unit = withContext(dispatcher) {

        val accountsList = accountsRepository.getAllByUser()
        val projectId = accountsList.first().projectId

        // Прогон всех аккаунтов в цикле
        accountsList.forEach { account ->
            if (account.accountType == YANDEX_DIRECT) { // Если аккаунт является аккаунтом яндекс директа, то выгружается стата по директу

                // Проверка, имеются ли выгруженные данные по аккаунту
                val isDataByAccountYDExists = statsStorage.checkAccountYD(account = account.name, project_id = projectId?:0)

                // Если данные поаккаунт имеются, то происходит выгрузка от последней даты обновления и до вчерашнего дня
                if (isDataByAccountYDExists){
                    // Здесь получается должно быть два периода обновления:
                        // 1.С поледней даты обновления по аккаунту и по вчера
                        // 2.С ранней даты обновления по аккаунту и по прошлый месяц (до 6 последних месяце в умолчанию)

                    // это процесс обновления с поледней даты и по вчера (вынести в отдельную функцию)
                    val lastUpdateDate = statsStorage.getLastUpdateDateYD(account = account.name, project_id = projectId?:0)
                    val yesterdayDate = LocalDateTime.now().minusDays(1).format(formatter)
                    if (lastUpdateDate != yesterdayDate){
                        for(day in 1..updateDays(lastUpdateDate)) {
                            val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                            val yandexDirectStats = yandexDirectStatsNetwork.getStats(
                                date = updateDate,
                                account = account.name,
                                token = account.accountToken ?: "",
                                projectId = projectId ?: 0
                            )
                            statsStorage.addYandexDirectData(data = yandexDirectStats)
                        }
                    }

                    // это процесс обновления с ранней даты и по начало месяца
                    // Здесь нужно получить:
                    // -раннюю дату обновления по аккаунту (Это будет 1 день месяцы)
                    val firstUpdateDate = statsStorage.getFirstUpdateDateYD(account = account.name, project_id = projectId?:0)

                    // -дату 1 дня прошлого месяца следующего за месяцем раннеей даты обновления,
                    // но не дальне 6 моследних месяцев от текущего месяа (констанотное значение,
                    // которое можно будет менить в настройках)
                    Log.d("MyLog", "firstUpdateDate: $firstUpdateDate by account: ${account.name}")
                    val startUpdateDate = updatePreviousDays(firstUpdateDate)
                    for (day in startUpdateDate.second..startUpdateDate.first){
                        val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                        Log.d("MyLog", "updateDate: $updateDate")

//                        val yandexDirectStats = yandexDirectStatsNetwork.getStats(
//                            date = updateDate,
//                            account = account.name,
//                            token = account.accountToken ?: "",
//                            projectId = projectId ?: 0
//                        )
//                        statsStorage.addYandexDirectData(data = yandexDirectStats)
                    }

                // Если данных по аккаунт нет, тогда выгрузка происходит с начала текущего месяца и до вчерашнего дня
                //----------------------------------------------------------------------------------
                //----------------------тут все-----------------------------------------------------
                //----------------------------------------------------------------------------------
                } else {
                    for(day in 1..updateNextDays()) {
                        val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                        val yandexDirectStats = yandexDirectStatsNetwork.getStats(
                            date = updateDate,
                            account = account.name,
                            token = account.accountToken ?: "",
                            projectId = projectId ?: 0
                        )
                        statsStorage.addYandexDirectData(data = yandexDirectStats)
                    }
                }
                //----------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------
            }

            //--------------------------------------------------------------------------------------
            if (account.accountType == YANDEX_METRIKA) { // Если аккаунт является аккаунтом яндекс метрики, то выгружается стата по метрике
                // Проверка, имеются ли выгруженные данные по аккаунту
                val isDataByCounterYMExists = statsStorage.checkCounterYM(
                    counter = account.metrikaCounter?:"",
                    project_id = projectId?:0
                )
                if (isDataByCounterYMExists){
                    val lastUpdateDate = statsStorage.getLastUpdateDateYM(
                        counter = account.metrikaCounter?:"",
                        project_id = projectId?:0
                    )
                    val yesterdayDate = LocalDateTime.now().minusDays(1).format(formatter)
                    if (lastUpdateDate != yesterdayDate){
                        for(day in 1..updateDays(lastUpdateDate)) {
                            val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                            val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
                                date = updateDate,
                                metrikaCounter = account.metrikaCounter?:"",
                                token = account.accountToken?:"",
                                projectId = projectId?:0
                            )
                            statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
                        }
                    }

                    val firstUpdateDate = statsStorage.getFirstUpdateDateYM(
                        counter = account.metrikaCounter?:"",
                        project_id = projectId?:0
                    )
                    Log.d("MyLog", "firstUpdateDate: $firstUpdateDate by counter: ${account.metrikaCounter}")
                    val startUpdateDate = updatePreviousDays(firstUpdateDate)

                    for (day in startUpdateDate.second..startUpdateDate.first){
                        val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                        Log.d("MyLog", "updateDate: $updateDate")
//                        val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
//                            date = updateDate,
//                            metrikaCounter = account.metrikaCounter?:"",
//                            token = account.accountToken?:"",
//                            projectId = projectId?:0
//                        )
//                        statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
                    }
                //----------------------------------------------------------------------------------
                //----------------------тут все-----------------------------------------------------
                //----------------------------------------------------------------------------------
                } else {
                    for(date in 1..updateNextDays()) {
                        val updateDate = LocalDateTime.now().minusDays(date.toLong()).format(formatter)
                        val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
                            date = updateDate,
                            metrikaCounter = account.metrikaCounter?:"",
                            token = account.accountToken?:"",
                            projectId = projectId?:0
                        )
                        statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
                    }
                }
                //----------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------
            }
        }

        for(date in 1..updateNextDays()) {
            val updateDate = LocalDateTime.now().minusDays(date.toLong()).format(formatter)
            dataProcessing(updateDate = updateDate, projectId = projectId ?: 0)
        }
    }

    override suspend fun getGeneralStats(statsPeriod: Pair<String, String>): List<GeneralStats> {
        val accountsList = accountsRepository.getAllByUser()
        val projectId = accountsList.first().projectId
        return statsStorage.getGeneral(project_id = projectId?:0, stats_period = statsPeriod)
    }

    override suspend fun dataProcessing(updateDate: String, projectId: Int) {
        val yandexDidectDate = statsStorage.getYD(date = updateDate, project_id = projectId)
        val yandexMetrikaDate = statsStorage.getYM(date = updateDate, project_id = projectId)

        val generalStats = GeneralStats(
            date = updateDate,
            cost = yandexDidectDate.map { it.cost }.sumOf { it?:0 },
            impressions = yandexDidectDate.map { it.impressions }.sumOf { it?:0 },
            clicks = yandexDidectDate.map { it.clicks }.sumOf { it?:0 },
            transactions = yandexMetrikaDate.map { it.transactions }.sumOf { it?:0 },
            revenue = yandexMetrikaDate.map { it.revenue }.sumOf { it?:0 },
            project_id = projectId
        )
        statsStorage.addGeneralData(data = generalStats)
    }

    override suspend fun getStats(): List<YandexDirectStats> = withContext(dispatcher) {
        return@withContext listOf<YandexDirectStats>()
    }

    private fun updateDays(lastUpdateDate: String): Int {

        val yesterdayDate = LocalDateTime.now().minusDays(1).format(formatter)
        val date1 = LocalDate.of(
            lastUpdateDate.split("-")[0].toInt(),
            lastUpdateDate.split("-")[1].toInt(),
            lastUpdateDate.split("-")[2].toInt()
        )
        val date2 = LocalDate.of(
            yesterdayDate.split("-")[0].toInt(),
            yesterdayDate.split("-")[1].toInt(),
            yesterdayDate.split("-")[2].toInt()
        )
        return Period.between(date1, date2).days
    }

    fun updateNextDays(): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val firstDayMonth = YearMonth.now().atDay( 1 ).toString()
        val yesterday = LocalDateTime.now().minusDays(1).format(formatter)

        val firstTimestampInclusive = LocalDate.of(
            firstDayMonth.split("-")[0].toInt(),
            firstDayMonth.split("-")[1].toInt(),
            firstDayMonth.split("-")[2].toInt(),
        )
        val secondTimestampExclusive = LocalDate.of(
            yesterday.split("-")[0].toInt(),
            yesterday.split("-")[1].toInt(),
            yesterday.split("-")[2].toInt(),
        )
        val numberOfDays = Duration.between(
            firstTimestampInclusive.atStartOfDay(),
            secondTimestampExclusive.atStartOfDay())
            .toDays()
            .toInt()
            .inc()
        return numberOfDays
    }

    fun updatePreviousDays(firstUpdateDate: String): Pair<Int, Int> {

        // 1) Типа дата первого обновления аккаунта полученная из базы и дата за вчерашний день
        val yesterday = LocalDateTime.now().minusDays(1).format(formatter)

        // 2) Получение даты первого обновления аккаунта минус 1 месяц
        val startUpdateDate = LocalDate.of(
            firstUpdateDate.split("-")[0].toInt(),
            firstUpdateDate.split("-")[1].toInt(),
            firstUpdateDate.split("-")[2].toInt(),
        ).minusMonths(1)

        val endUpdateDate = LocalDate.of(
            firstUpdateDate.split("-")[0].toInt(),
            firstUpdateDate.split("-")[1].toInt(),
            firstUpdateDate.split("-")[2].toInt(),
        )

        // 3) Вычисление количества дней между вчерашнем днем и датой перого обновления базы (минус 1 месяц)
        val secondTimestampExclusive = LocalDate.of(
            yesterday.split("-")[0].toInt(),
            yesterday.split("-")[1].toInt(),
            yesterday.split("-")[2].toInt(),
        )

        val firstUpdateDays = Duration.between(
            startUpdateDate.atStartOfDay(),
            secondTimestampExclusive.atStartOfDay())
            .toDays()
            .toInt()
            .inc()

        val lastUpdateDays = Duration.between(
            endUpdateDate.atStartOfDay(),
            secondTimestampExclusive.atStartOfDay())
            .toDays()
            .toInt().inc()
            .inc()

        return Pair(firstUpdateDays, lastUpdateDays)
    }

}