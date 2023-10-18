package com.example.amanda.gachaSystem

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GachaApi {
    @POST("activateGacha")
    suspend fun activateGacha(@Body request: GachaRequest): Response<GachaResponse>
}