package com.example.greetingcard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.greetingcard.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [SignalMapEntity::class, UserSignalEntity::class, MapSize::class], version = 5, exportSchema = false)
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
                    .build()

                INSTANCE = instance

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val dao = instance.appDao()
                        val apiService = ApiService.create()
                        val mapSize = apiService.getMapSize()
                        dao.insertMapSize(MapSize(
                            minX =  mapSize.minX,
                            minY = mapSize.minY,
                            maxX = mapSize.maxX,
                            maxY = mapSize.maxY
                        ))

                        for(i in mapSize.minX..mapSize.maxX){
                            val xThSignalMapCell = apiService.getMapColumn(i)
                            for(mapCell in xThSignalMapCell){
                                dao.insertMapCell(SignalMapEntity(
                                    x = mapCell.x,
                                    y = mapCell.y,
                                    strength1 = mapCell.strength1,
                                    strength2 = mapCell.strength2,
                                    strength3 = mapCell.strength3
                                ))
                            }

                        }

                        val testUsers = apiService.getTestUsers()
                        for (user in testUsers) {
                            dao.insertUser(
                                UserSignalEntity(
                                    mac = user.mac,
                                    sensor = user.sensor,
                                    stiprumas = user.stiprumas,
                                    estimatedX = user.estimatedX,
                                    estimatedY = user.estimatedY
                                )
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                instance
            }
        }
    }
}