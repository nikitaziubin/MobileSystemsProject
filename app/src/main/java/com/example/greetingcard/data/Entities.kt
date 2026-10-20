package com.example.greetingcard.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "signal_map")
data class SignalMapEntity(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val x: Int,
    val y: Int,
    val strength1: Int,
    val strength2: Int,
    val strength3: Int

)

@Entity(tableName = "vartotojai")
data class UserSignalEntity(
    @PrimaryKey val mac : String,
    val sensor: String,
    val stiprumas: Int,
    val estimatedX: Int,
    val estimatedY: Int
)

@Entity(tableName = "map_size")
data class MapSize(
    @PrimaryKey val id: Int = 1,
    val minX: Int,
    val minY : Int,
    val maxX: Int,
    val maxY: Int
)