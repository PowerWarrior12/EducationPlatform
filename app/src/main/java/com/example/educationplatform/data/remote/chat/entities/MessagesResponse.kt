package com.example.educationplatform.data.remote.chat.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessagesResponse(
    val messages: List<MessageResponse>
)
