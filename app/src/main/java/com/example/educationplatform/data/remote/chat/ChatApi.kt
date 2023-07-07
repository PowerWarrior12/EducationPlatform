package com.example.educationplatform.data.remote.chat

import com.example.educationplatform.data.remote.chat.entities.ChatResponse
import com.example.educationplatform.data.remote.chat.entities.CreateMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApi {
    @GET("/chat/my/list")
    suspend fun getUserChats(): Response<List<ChatResponse>>

    @POST("")
    suspend fun createMessage(@Body message: CreateMessageRequest): Response<Unit>
}