package com.example.educationplatform.data.remote.chat.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatsResponse(
    @Json(name = "chats")
    val chats: List<ChatResponse>
)