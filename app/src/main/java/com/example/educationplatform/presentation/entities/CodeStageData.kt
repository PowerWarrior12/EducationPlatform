package com.example.educationplatform.presentation.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CodeStageData (
    @Json(name = "testData")
    val testData: List<TestData>,
    @Json(name = "code")
    val code: String,
    @Json(name = "task")
    val task: String,
    @Json(name = "language")
    val language: String
)