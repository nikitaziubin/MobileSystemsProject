package com.example.greetingcard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.AppDatabase
import com.example.greetingcard.data.MapSize
import com.example.greetingcard.data.SignalMapEntity
import com.example.greetingcard.data.SignalsForUserLocationFindings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocationViewModel(application: Application): AndroidViewModel(application) {
    private val dao = AppDatabase.getDataBase(application).appDao()

    val signalMap: StateFlow<List<SignalMapEntity>> = dao.getSignalMap()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val users: StateFlow<List<SignalsForUserLocationFindings>> = dao.getSignalsForUserLocationFindings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mapSize: StateFlow<MapSize> = dao.getMapSize()
        .filterNotNull()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MapSize(1,-5,-11,6, 35))

    fun addOrUpdateUserLocation(mac: String, strength1: Int, strength2: Int, strength3: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertSignalsForUserLocationFindings(
                SignalsForUserLocationFindings(
                    mac = mac,
                    strength1 = strength1,
                    strength2 = strength2,
                    strength3 = strength3,
                )
            )
        }
    }
}