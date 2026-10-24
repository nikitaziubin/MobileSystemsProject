package com.example.greetingcard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.AppDatabase
import com.example.greetingcard.data.MapSize
import com.example.greetingcard.data.SignalMapEntity
import com.example.greetingcard.data.SignalsForUserLocationFindings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.milliseconds

data class NNNSResult(
    var x: Int,
    var y: Int,
    var minRes: Double
)

class LocationViewModel(application: Application): AndroidViewModel(application) {
    private val dao = AppDatabase.getDataBase(application).appDao()

    val signalMap: StateFlow<List<SignalMapEntity>> = dao.getSignalMap()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val users: StateFlow<List<SignalsForUserLocationFindings>> = dao.getSignalsForUserLocationFindings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mapSize: StateFlow<MapSize> = dao.getMapSize()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MapSize(1,-5,-11,6, 35))

    private val _highlightedCell = MutableStateFlow<Pair<Int, Int>?>(null)
    val highlightedCell: MutableStateFlow<Pair<Int, Int>?> = _highlightedCell

    private var highlightedJob: Job? = null
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

    fun findLocationByStrength(inputSignal: SignalsForUserLocationFindings) : NNNSResult{
        val nnns = NNNSResult(
            x = 0,
            y = 0,
            minRes = Double.MAX_VALUE
        )
        for(signal in signalMap.value.toList()){
            val d1 = (inputSignal.strength1 - signal.strength1).toDouble()
            val d2 = (inputSignal.strength2 - signal.strength2).toDouble()
            val d3 = (inputSignal.strength3 - signal.strength3).toDouble()

            val res = sqrt(d1.pow(2)+d2.pow(2)+d3.pow(2))
            if(res < nnns.minRes){
                nnns.x = signal.x
                nnns.y = signal.y
                nnns.minRes = res
            }
        }
        return nnns
    }

    fun calculateAndHighlightLocation(user: SignalsForUserLocationFindings){
        val result = findLocationByStrength(user)
        _highlightedCell.value = Pair(result.x, result.y)

        highlightedJob?.cancel()
        highlightedJob = viewModelScope.launch {
            delay(10000L.milliseconds)
            _highlightedCell.value = null
        }
    }
}