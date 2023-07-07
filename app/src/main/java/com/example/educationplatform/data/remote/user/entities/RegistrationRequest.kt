package com.example.educationplatform.data.remote.user.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "secondName")
    val secondName: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)
