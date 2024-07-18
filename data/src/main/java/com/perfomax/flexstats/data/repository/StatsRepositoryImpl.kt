package com.perfomax.flexstats.data.repository

import android.util.Log
import com.perfomax.flexstats.core.contracts.DATE_FORMAT
import com.perfomax.flexstats.core.contracts.YANDEX_DIRECT
import com.perfomax.flexstats.core.contracts.YANDEX_METRIKA
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.network.YandexMetrikaStatsNetwork
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.data_api.storage.StatsStorage
import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
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
    val yesterdayDate = LocalDateTime.now().minusDays(1).format(formatter)

    //Добавить логику - если данны обновлены по вчерашний день, то обновлять не нужно
    override suspend fun updateStats(): Unit = withContext(dispatcher) {

        val accountsList = accountsRepository.getAllByUser()
        val projectId = accountsList.first().projectId

        // Прогон всех аккаунтов в цикле
        accountsList.forEach { account ->
            if (account.accountType == YANDEX_DIRECT) yandexDirectStatsUpdate(account = account, project_id = projectId?:0)
            if (account.accountType == YANDEX_METRIKA) yandexMetrikaStatsUpdate(account = account, project_id = projectId?:0)
        }
        dataProcessingNew(projectId = projectId?:0)

        // Нужно вычислять...
        // сначала вы обновление от последней даты, до вчерашней, т.е. "обновление вперед"
//        for(date in 1..updateNextDays()) {
//            val updateDate = LocalDateTime.now().minusDays(date.toLong()).format(formatter)
//            Log.d("MyLog", "updateNextDays(): ${updateNextDays()}")
//            Log.d("MyLog", "updateDate: $updateDate")
//            dataProcessing(updateDate = updateDate, projectId = projectId ?: 0)
//        }

        // далее нужно "обновление назад" т.е. от ранней даты обновления в таблице GeneralStats()
        // до ранней даты обновления в таблицах yandexDidectDate и yandexMetrikaDate

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


    private suspend fun yandexDirectStatsUpdate(account: Account, project_id: Int) {
        // Проверка, имеются ли выгруженные данные по аккаунту
        val isDataByAccountYDExists = statsStorage.checkAccountYD(account = account.name, project_id = project_id)

        // Если данные поаккаунт имеются, то происходит выгрузка от последней даты обновления и до вчерашнего дня
        if (isDataByAccountYDExists){
            // Здесь получается должно быть два периода обновления:
            // 1.С поледней даты обновления по аккаунту и по вчера
            // 2.С ранней даты обновления по аккаунту и по прошлый месяц (до 6 последних месяце в умолчанию)

            // это процесс обновления с поледней даты и по вчера (вынести в отдельную функцию)
            val lastUpdateDate = statsStorage.getLastUpdateDateYD(account = account.name, project_id = project_id)
            if (lastUpdateDate != yesterdayDate){
                for(day in 1..updateDays(lastUpdateDate)) {
                    val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                    val yandexDirectStats = yandexDirectStatsNetwork.getStats(
                        date = updateDate,
                        account = account.name,
                        token = account.accountToken ?:"",
                        projectId = project_id
                    )
                    statsStorage.addYandexDirectData(data = yandexDirectStats)
                }
            }
            // это процесс обновления с ранней даты и по начало месяца
            // Здесь нужно получить:
            // -раннюю дату обновления по аккаунту (Это будет 1 день месяцы)
            val firstUpdateDate = statsStorage.getFirstUpdateDateYD(account = account.name, project_id = project_id)

            // -дату 1 дня прошлого месяца следующего за месяцем раннеей даты обновления,
            // но не дальне 6 моследних месяцев от текущего месяа (констанотное значение,
            // которое можно будет менить в настройках)

            val startUpdateDate = updatePreviousDays(firstUpdateDate)
            for (day in startUpdateDate.second..startUpdateDate.first){
                val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                Log.d("MyLog", "updateDate: $updateDate by account: ${account.name}")
                val yandexDirectStats = yandexDirectStatsNetwork.getStats(
                    date = updateDate,
                    account = account.name,
                    token = account.accountToken ?: "",
                    projectId = project_id
                )
                statsStorage.addYandexDirectData(data = yandexDirectStats)
            }
        } else {
            for(day in 1..updateNextDays()) {
                val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                val yandexDirectStats = yandexDirectStatsNetwork.getStats(
                    date = updateDate,
                    account = account.name,
                    token = account.accountToken ?: "",
                    projectId = project_id
                )
                statsStorage.addYandexDirectData(data = yandexDirectStats)
            }
        }
    }

    private suspend fun yandexMetrikaStatsUpdate(account: Account, project_id: Int){
        // Если аккаунт является аккаунтом яндекс метрики, то выгружается стата по метрике
        // Проверка, имеются ли выгруженные данные по аккаунту
        val isDataByCounterYMExists = statsStorage.checkCounterYM(
            counter = account.metrikaCounter?:"",
            project_id = project_id
        )
        if (isDataByCounterYMExists){
            val lastUpdateDate = statsStorage.getLastUpdateDateYM(
                counter = account.metrikaCounter?:"",
                project_id = project_id
            )
            if (lastUpdateDate != yesterdayDate){
                for(day in 1..updateDays(lastUpdateDate)) {
                    val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                    Log.d("MyLog", "updateDate: $updateDate by metrika_counter: ${account.metrikaCounter}")
                    val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
                        date = updateDate,
                        metrikaCounter = account.metrikaCounter?:"",
                        token = account.accountToken?:"",
                        projectId = project_id
                    )
                    statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
                }
            }

            val firstUpdateDate = statsStorage.getFirstUpdateDateYM(
                counter = account.metrikaCounter?:"",
                project_id = project_id
            )
            Log.d("MyLog", "firstUpdateDate: $firstUpdateDate by counter: ${account.metrikaCounter}")
            val startUpdateDate = updatePreviousDays(firstUpdateDate)

            for (day in startUpdateDate.second..startUpdateDate.first){
                val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
                Log.d("MyLog", "updateDate: $updateDate")
                        val yandexMetrikaStats = yandexMetrikaStatsNetwork.getStats(
                            date = updateDate,
                            metrikaCounter = account.metrikaCounter?:"",
                            token = account.accountToken?:"",
                            projectId = project_id
                        )
                        statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
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
                    projectId = project_id
                )
                statsStorage.addYandexMetrikaData(data = yandexMetrikaStats)
            }
        }

    }

    private suspend fun dataProcessingNew(projectId: Int) {
        val firstUpdateDate = statsStorage.getFirstUpdateDateGeneral(projectId)
        // итак у нас есть дата за вчера и дата наиболее раннего обновления аккаунту
        // Нужно получить промежуток дней между вчерашней датой и
        Log.d("MyLog", "firstUpdateDate in StatsRepository $firstUpdateDate")
        val firstTimestampInclusive = LocalDate.of(
            firstUpdateDate.split("-")[0].toInt(),
            firstUpdateDate.split("-")[1].toInt(),
            firstUpdateDate.split("-")[2].toInt(),
        )
        val secondTimestampExclusive = LocalDate.of(
            yesterdayDate.split("-")[0].toInt(),
            yesterdayDate.split("-")[1].toInt(),
            yesterdayDate.split("-")[2].toInt(),
        )
        val numberOfDays = Duration.between(
            firstTimestampInclusive.atStartOfDay(),
            secondTimestampExclusive.atStartOfDay())
            .toDays()
            .toInt()
            .inc()

        for(date in 1..numberOfDays) {

            val updateDate = LocalDateTime.now().minusDays(date.toLong()).format(formatter)
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
//
//        // далее нужно "обновление назад" т.е. от ранней даты обновления в таблице GeneralStats()
//        // до ранней даты обновления в таблицах yandexDidectDate и yandexMetrikaDate


    }

    private fun updateDays(lastUpdateDate: String): Int {
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
        val firstDayMonth = YearMonth.now().atDay( 1 ).toString()
        val firstTimestampInclusive = LocalDate.of(
            firstDayMonth.split("-")[0].toInt(),
            firstDayMonth.split("-")[1].toInt(),
            firstDayMonth.split("-")[2].toInt(),
        )
        val secondTimestampExclusive = LocalDate.of(
            yesterdayDate.split("-")[0].toInt(),
            yesterdayDate.split("-")[1].toInt(),
            yesterdayDate.split("-")[2].toInt(),
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
            yesterdayDate.split("-")[0].toInt(),
            yesterdayDate.split("-")[1].toInt(),
            yesterdayDate.split("-")[2].toInt(),
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