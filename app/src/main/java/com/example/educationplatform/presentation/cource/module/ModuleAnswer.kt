package com.example.educationplatform.presentation.cource.module

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModuleAnswer(
    @Json(name = "answer")
    val answer: String,
    @Json(name = "message")
    val message: String
)
