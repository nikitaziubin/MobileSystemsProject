package com.example.greetingcard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.AppDatabase
import com.example.greetingcard.data.MapSize
import com.example.greetingcard.data.SignalMapEntity
import com.example.greetingcard.data.UserSignalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocationViewModel(application: Application): AndroidViewModel(application) {
    private val dao = AppDatabase.getDataBase(application).appDao()

    val signalMap: StateFlow<List<SignalMapEntity>> = dao.getSignalMap()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<SignalMapEntity>())

    val users: StateFlow<List<UserSignalEntity>> = dao.getUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<UserSignalEntity>())

    val mapSize: StateFlow<MapSize> = dao.getMapSize()
        .filterNotNull()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MapSize(1,-5,-11,6, 35))

    fun addOrUpdateUserLocation(mac: String, sensor: String, strength: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val estimatedX = 1
            val estimatedY = 5
            dao.insertUser(
                UserSignalEntity(
                    mac = mac,
                    sensor = sensor,
                    stiprumas = strength,
                    estimatedX = estimatedX,
                    estimatedY = estimatedY
                )
            )
        }
    }
}