package com.example.educationplatform.presentation.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TestData(
    @Json(name = "input")
    val inputData: String,
    @Json(name = "output")
    val outputData: String
)
