package com.example.educationplatform.data.remote.direction

import retrofit2.Response
import retrofit2.http.GET

interface DirectionApi {
    @GET("direction/list")
    suspend fun getDirections(): Response<List<String>>
}