package com.example.educationplatform.data.remote.chat.entities

import androidx.annotation.Nullable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "courceId")
    val courseId: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "imageSrc")
    val icon: String,
    @Nullable
    @Json(name = "lastMessage")
    val lastMessage: MessageResponse?
)
