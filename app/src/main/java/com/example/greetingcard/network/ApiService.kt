package com.example.greetingcard.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class RemoteMapCell(val x: Int, val y: Int, val strength1: Int, val strength2: Int, val strength3: Int)
data class RemoteSignal(val mac: String, val strength1: Int, val strength2: Int, val strength3: Int)

data class RemoteMapSize(val minX: Int, val minY : Int, val maxX: Int, val maxY: Int)

interface ApiService {
    @GET("map-column-agg")
    suspend fun getMapColumn(@Query("x") x: Int): List<RemoteMapCell>

    @GET("map-size")
    suspend fun getMapSize(): RemoteMapSize

    @GET("test-users-agg")
    suspend fun getRemoteSignal(): List<RemoteSignal>

    companion object {
        const val BASE_URL = "http://192.168.137.1:9000/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}