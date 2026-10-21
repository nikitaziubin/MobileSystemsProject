package com.example.greetingcard.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao{
    @Query("SELECT * FROM signal_map")
    fun getSignalMap(): Flow<List<SignalMapEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMapCell(map: SignalMapEntity)

    @Query("SELECT * FROM signals_for_user_location_findings")
    fun getSignalsForUserLocationFindings(): Flow<List<SignalsForUserLocationFindings>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignalsForUserLocationFindings(user: SignalsForUserLocationFindings)

    @Query("SELECT * FROM map_size WHERE id = 1")
    fun getMapSize(): Flow<MapSize>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMapSize(mapSize: MapSize)
}