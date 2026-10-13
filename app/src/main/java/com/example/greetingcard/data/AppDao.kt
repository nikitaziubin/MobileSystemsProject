package com.example.greetingcard.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM signal_map")
    fun getSignalMap(): Flow<List<SignalMapEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMap(map: List<SignalMapEntity>)

    @Query("SELECT * FROM vartotojai")
    fun getUsers(): Flow<List<UserSignalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserSignalEntity)
}