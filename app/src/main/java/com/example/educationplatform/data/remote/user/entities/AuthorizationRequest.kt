package com.example.educationplatform.data.remote.user.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthorizationRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)
