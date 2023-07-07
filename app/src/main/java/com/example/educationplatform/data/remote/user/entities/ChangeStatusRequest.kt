package com.example.educationplatform.data.remote.user.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangeStatusRequest (
    @Json(name = "message")
    val message: String
)