package com.example.amanda.gachaSystem

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {

    val gachaApi: GachaApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.15.108")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(GachaApi::class.java)
    }
}