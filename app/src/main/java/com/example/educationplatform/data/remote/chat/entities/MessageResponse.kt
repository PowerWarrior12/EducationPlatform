package com.example.educationplatform.data.remote.chat.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MessageResponse(
    @Json(name = "id")
    val id: Int,
    //@Json(name = "chatId")
    //val chatId: Int,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "userName")
    val userName: String,
    //@Json(name = "userIcon")
    //val userIcon: String,
    @Json(name = "text")
    val text: String
)