package com.example.educationplatform.data.remote.chat.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateMessageRequest(
    @Json(name = "text")
    val text: String,
    @Json(name = "chatId")
    val chatId: Int
)
