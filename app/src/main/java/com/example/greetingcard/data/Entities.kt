package com.example.greetingcard.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "signal_map", primaryKeys = ["x", "y"])
data class SignalMapEntity(
    val x: Int,
    val y: Int,
    val strength1: Int,
    val strength2: Int,
    val strength3: Int

)

@Entity(tableName = "signals_for_user_location_findings")
data class SignalsForUserLocationFindings(
    @PrimaryKey val mac : String,
    val strength1: Int,
    val strength2: Int,
    val strength3: Int
)

@Entity(tableName = "map_size")
data class MapSize(
    @PrimaryKey val id: Int = 1,
    val minX: Int,
    val minY : Int,
    val maxX: Int,
    val maxY: Int
)