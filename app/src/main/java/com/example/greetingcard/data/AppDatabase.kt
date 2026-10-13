package com.example.greetingcard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [SignalMapEntity::class, UserSignalEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDataBase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "location_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    populateInitialData(database.appDao())
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                })
                .build()

                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(dao: AppDao) {
            val mapList = mutableListOf<SignalMapEntity>()
            for (y in -11..19) {
                for (x in -5..6) {
                    val trained = (x == 1 || x == 2) && (y % 2 == 0)
                    mapList.add(
                        SignalMapEntity(
                            x = x,
                            y = y,
                            isTrained = trained,
                            apName = "seklys_drone",
                            strength = if (trained) -50 else -90
                        )
                    )
                }
            }
            dao.insertMap(mapList)

            dao.insertUser(UserSignalEntity(mac = "AA:BB:CC:DD:EE:FF", sensor = "seklys_drone", stiprumas = -52, estimatedX = 1, estimatedY = 17))
            dao.insertUser(UserSignalEntity(mac = "11:22:33:44:55:66", sensor = "seklys_drone", stiprumas = -65, estimatedX = 2, estimatedY = 8))
        }
    }
}
