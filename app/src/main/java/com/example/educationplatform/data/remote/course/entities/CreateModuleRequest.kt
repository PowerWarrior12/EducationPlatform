package com.example.educationplatform.data.remote.course.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateModuleRequest(
    @Json(name = "title")
    val title: String,
    @Json(name = "info")
    val info: String,
    @Json(name = "score")
    val score: Int
)
