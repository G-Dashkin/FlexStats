package com.perfomax.flexstats.data.database.factory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.perfomax.flexstats.data.database.dao.AccountsDao
import com.perfomax.flexstats.data.database.dao.AuthDao
import com.perfomax.flexstats.data.database.dao.ProjectsDao
import com.perfomax.flexstats.data.database.dao.StatsDao
import com.perfomax.flexstats.data.database.entities.AccountEntity
import com.perfomax.flexstats.data.database.entities.GeneralStatsEntity
import com.perfomax.flexstats.data.database.entities.ProjectEntity
import com.perfomax.flexstats.data.database.entities.UserEntity
import com.perfomax.flexstats.data.database.entities.YandexDirectStatsEntity
import com.perfomax.flexstats.data.database.entities.YandexMetrikaStatsEntity

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "flexstats.db"

@Database(
    entities = [
        UserEntity::class,
        ProjectEntity::class,
        AccountEntity::class,

        YandexDirectStatsEntity::class,
        YandexMetrikaStatsEntity::class,
        GeneralStatsEntity::class
    ],
    exportSchema = false,
    version = DATABASE_VERSION)
abstract class AppDatabase: RoomDatabase() {

    abstract fun authDao(): AuthDao
    abstract fun projectsDao(): ProjectsDao
    abstract fun accountsDao(): AccountsDao
    abstract fun statsDao(): StatsDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun createDatabase(context: Context): AppDatabase {
            return instance?: synchronized(LOCK) {
                Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = DATABASE_NAME
                )
                    .createFromAsset("database/$DATABASE_NAME")
                    .build()
            }
        }
    }
}