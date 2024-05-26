package com.perfomax.flexstats.di

import android.app.Application
import android.content.Context
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.data.database.dao.AuthDao
import com.perfomax.flexstats.data.database.factory.AppDatabase
import com.perfomax.flexstats.data.repository.AuthRepositoryImpl
import com.perfomax.flexstats.data.storage.AuthStorageImpl
import com.perfomax.flexstats.presentation.navigation.NavigatorLifecycle
import com.perfomax.flexstats.presentation.navigation.RouterImpl
import com.perfomax.flextats.data_api.repository.AuthRepository
import com.perfomax.flextats.data_api.storage.AuthStorage
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
    fun provideAppDatabase(): AppDatabase = AppDatabase.createDatabase(context = application)

    // Auth provides--------------------------------------------------------------------------------
    @Provides
    @Singleton
    fun provideAuthDao(
        db: AppDatabase
    ): AuthDao = db.authDao()

    @Singleton
    @Provides
    fun provideAuthRepository(
        authStorage: AuthStorage
    ): AuthRepository = AuthRepositoryImpl(authStorage)

    @Singleton
    @Provides
    fun provideAuthStorage(
        authDao: AuthDao
    ): AuthStorage = AuthStorageImpl(authDao)

}