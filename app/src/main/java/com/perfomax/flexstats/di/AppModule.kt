package com.perfomax.flexstats.di

import android.app.Application
import android.content.Context
import com.perfomax.flexstats.app.App
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.data.database.dao.AccountsDao
import com.perfomax.flexstats.data.database.dao.AuthDao
import com.perfomax.flexstats.data.database.dao.ProjectsDao
import com.perfomax.flexstats.data.database.dao.YandexDirectStatsDao
import com.perfomax.flexstats.data.database.factory.AppDatabase
import com.perfomax.flexstats.data.datastore.SettingsDataStoreImpl
import com.perfomax.flexstats.data.network.retrofit.YandexAccessToken.YandexAccessTokenNetworkImpl
import com.perfomax.flexstats.data.network.retrofit.YandexDirectStats.YandexDirectStatsNetworkImpl
import com.perfomax.flexstats.data.repository.AccountsRepositoryImpl
import com.perfomax.flexstats.data.repository.AuthRepositoryImpl
import com.perfomax.flexstats.data.repository.ProjectsRepositoryImpl
import com.perfomax.flexstats.data.repository.StatsRepositoryImpl
import com.perfomax.flexstats.data.storage.AccountsStorageImpl
import com.perfomax.flexstats.data.storage.AuthStorageImpl
import com.perfomax.flexstats.data.storage.ProjectsStorageImpl
import com.perfomax.flexstats.data_api.datastore.SettingsDataStore
import com.perfomax.flexstats.data_api.network.YandexAccessTokenNetwork
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.presentation.navigation.NavigatorLifecycle
import com.perfomax.flexstats.presentation.navigation.RouterImpl
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.data_api.storage.AccountsStorage
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.data_api.storage.ProjectsStorage
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {


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

    @Provides
    @Singleton
    fun provideSettingsDataStore(): SettingsDataStore = SettingsDataStoreImpl(context = App.instance)

    @Singleton
    @Provides
    fun provideAuthStorage(
        authDao: AuthDao
    ): AuthStorage = AuthStorageImpl(authDao = authDao)

    @Singleton
    @Provides
    fun provideAuthRepository(
        authStorage: AuthStorage
    ): AuthRepository = AuthRepositoryImpl(authStorage)

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
    @Singleton
    @Provides
    fun provideNetworkStorage(): YandexAccessTokenNetwork = YandexAccessTokenNetworkImpl()

    @Singleton
    @Provides
    fun provideYandexDirectStatsNetwork(
        yandexDirectStatsDao: YandexDirectStatsDao,
        authStorage: AuthStorage
    ): YandexDirectStatsNetwork = YandexDirectStatsNetworkImpl(
        yandexDirectStatsDao = yandexDirectStatsDao,
        authStorage = authStorage
    )

    // Stats provides-----------------------------------------------------------------------------
    @Provides
    @Singleton
    fun provideYandexDirectStatsDao(db: AppDatabase): YandexDirectStatsDao = db.yandexDirectStatsDao()

    @Singleton
    @Provides
    fun provideStatsRepository(
        yandexDirectStatsNetwork: YandexDirectStatsNetwork,
        yandexDirectStatsDao: YandexDirectStatsDao,
        accountsRepository: AccountsRepository
    ): StatsRepository = StatsRepositoryImpl(
        yandexDirectStatsNetwork = yandexDirectStatsNetwork,
        yandexDirectStatsDao = yandexDirectStatsDao,
        accountsRepository = accountsRepository
    )

}