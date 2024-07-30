package com.perfomax.flexstats.di

import android.app.Application
import android.content.Context
import com.perfomax.flexstats.app.App
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.data.database.dao.AccountsDao
import com.perfomax.flexstats.data.database.dao.AuthDao
import com.perfomax.flexstats.data.database.dao.ProjectsDao
import com.perfomax.flexstats.data.database.dao.StatsDao
import com.perfomax.flexstats.data.database.factory.AppDatabase
import com.perfomax.flexstats.data.network.retrofit.yandex_access_token.YandexAccessTokenNetworkImpl
import com.perfomax.flexstats.data.network.retrofit.yandex_direct_stats.YandexDirectStatsNetworkImpl
import com.perfomax.flexstats.data.network.retrofit.yandex_metrika_stats.YandexMetrikaStatsNetworkImpl
import com.perfomax.flexstats.data.repository.AccountsRepositoryImpl
import com.perfomax.flexstats.data.repository.AuthRepositoryImpl
import com.perfomax.flexstats.data.repository.ProjectsRepositoryImpl
import com.perfomax.flexstats.data.repository.StatsRepositoryImpl
import com.perfomax.flexstats.data.storage.AccountsStorageImpl
import com.perfomax.flexstats.data.storage.AuthStorageImpl
import com.perfomax.flexstats.data.storage.ProjectsStorageImpl
import com.perfomax.flexstats.data.storage.StatsStorageImpl
import com.perfomax.flexstats.data_api.network.YandexAccessTokenNetwork
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.network.YandexMetrikaStatsNetwork
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.presentation.navigation.NavigatorLifecycle
import com.perfomax.flexstats.presentation.navigation.RouterImpl
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.data_api.storage.AccountsStorage
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.data_api.storage.ProjectsStorage
import com.perfomax.flexstats.data_api.storage.StatsStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    // Main provides--------------------------------------------------------------------------------
    @Singleton
    @Provides
    fun provideRouterImpl(): RouterImpl {
        return RouterImpl()
    }

    @Provides
    fun provideRouter(routerImpl: RouterImpl): Router {
        return routerImpl
    }

    @Provides
    fun provideNavigatorLifecycle(routerImpl: RouterImpl): NavigatorLifecycle {
        return routerImpl
    }

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase = AppDatabase.createDatabase(context = App.instance)

    // Auth provides--------------------------------------------------------------------------------
    @Provides
    @Singleton
    fun provideAuthDao(db: AppDatabase): AuthDao = db.authDao()

    @Singleton
    @Provides
    fun provideAuthStorage(
        authDao: AuthDao
    ): AuthStorage = AuthStorageImpl(authDao = authDao)

    @Singleton
    @Provides
    fun provideAuthRepository(
        authStorage: AuthStorage,
        context: Context
    ): AuthRepository = AuthRepositoryImpl(authStorage, context)

    // Projects provides----------------------------------------------------------------------------
    @Provides
    @Singleton
    fun provideProjectsDao(db: AppDatabase): ProjectsDao = db.projectsDao()

    @Singleton
    @Provides
    fun provideProjectsStorage(
        projectsDao: ProjectsDao
    ): ProjectsStorage = ProjectsStorageImpl(projectsDao = projectsDao)

    @Singleton
    @Provides
    fun provideProjectsRepository(
        projectsStorage: ProjectsStorage,
        authStorage: AuthStorage
    ): ProjectsRepository = ProjectsRepositoryImpl(projectsStorage = projectsStorage, authStorage = authStorage)

    // Accounts provides----------------------------------------------------------------------------
    @Provides
    @Singleton
    fun provideAccountsDao(db: AppDatabase): AccountsDao = db.accountsDao()

    @Provides
    @Singleton
    fun provideAccountsStorage(
        accountsDao: AccountsDao
    ): AccountsStorage = AccountsStorageImpl(accountsDao =  accountsDao)

    @Singleton
    @Provides
    fun provideAccountsRepository(
        projectsStorage: ProjectsStorage,
        accountsStorage: AccountsStorage,
        authStorage: AuthStorage,
        yandexAccessTokenNetwork: YandexAccessTokenNetwork
    ): AccountsRepository = AccountsRepositoryImpl(
        projectsStorage =  projectsStorage,
        accountsStorage = accountsStorage,
        authStorage = authStorage,
        yandexAccessTokenNetwork = yandexAccessTokenNetwork
    )

    // Network provides----------------------------------------------------------------------------
    @Provides
    fun provideYandexAccessTokenNetwork(): YandexAccessTokenNetwork = YandexAccessTokenNetworkImpl()

    @Provides
    fun provideYandexDirectStatsNetwork(): YandexDirectStatsNetwork = YandexDirectStatsNetworkImpl()

    @Provides
    fun provideYandexMetrikaStatsNetwork(): YandexMetrikaStatsNetwork = YandexMetrikaStatsNetworkImpl()

    // Stats provides-----------------------------------------------------------------------------
    @Provides
    @Singleton
    fun provideStatsDao(db: AppDatabase): StatsDao = db.statsDao()

    @Provides
    @Singleton
    fun provideStatsStorage(
        statsDao: StatsDao
    ): StatsStorage = StatsStorageImpl(statsDao = statsDao)

    @Singleton
    @Provides
    fun provideStatsRepository(
        yandexDirectStatsNetwork: YandexDirectStatsNetwork,
        yandexMetrikaStatsNetwork: YandexMetrikaStatsNetwork,
        accountsRepository: AccountsRepository,
        statsStorage: StatsStorage
    ): StatsRepository = StatsRepositoryImpl(
        yandexDirectStatsNetwork = yandexDirectStatsNetwork,
        yandexMetrikaStatsNetwork = yandexMetrikaStatsNetwork,
        accountsRepository = accountsRepository,
        statsStorage = statsStorage
    )

}