package com.perfomax.flexstats.di

import android.app.Application
import android.content.Context
import com.perfomax.flexstats.app.App
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.data.database.dao.AuthDao
import com.perfomax.flexstats.data.database.dao.ProjectsDao
import com.perfomax.flexstats.data.database.factory.AppDatabase
import com.perfomax.flexstats.data.datastore.AuthDataStoreImpl
import com.perfomax.flexstats.data.repository.AuthRepositoryImpl
import com.perfomax.flexstats.data.repository.ProjectsRepositoryImpl
import com.perfomax.flexstats.data.storage.AuthStorageImpl
import com.perfomax.flexstats.data.storage.ProjectsStorageImpl
import com.perfomax.flexstats.presentation.navigation.NavigatorLifecycle
import com.perfomax.flexstats.presentation.navigation.RouterImpl
import com.perfomax.flexstats.data_api.datastore.AuthDataStore
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.data_api.storage.ProjectsStorage
import dagger.Module
import dagger.Provides
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
    fun provideAuthDataStore(): AuthDataStore = AuthDataStoreImpl(context = App.instance)

    @Singleton
    @Provides
    fun provideAuthStorage(
        authDao: AuthDao,
        authDataStore: AuthDataStore
    ): AuthStorage = AuthStorageImpl(authDao = authDao, authDataStore = authDataStore)

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
        projectsStorage: ProjectsStorage
    ): ProjectsRepository = ProjectsRepositoryImpl(projectsStorage)
}