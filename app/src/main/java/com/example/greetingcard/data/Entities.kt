package com.example.greetingcard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signal_map")
data class SignalMapEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val x: Int,
    val y: Int,
    val isTrained: Boolean,
    val apName: String,
    val strength: Int
)

@Entity(tableName = "vartotojai")
data class UserSignalEntity(
    @PrimaryKey val mac: String,
    val sensor: String,
    val stiprumas: Int,
    val estimatedX: Int,
    val estimatedY: Int
)