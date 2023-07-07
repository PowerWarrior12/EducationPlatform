package com.example.educationplatform.presentation.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlockStageData (
    @Json(name = "testData")
    val testData: List<TestData>,
    @Json(name = "diagramData")
    val diagramData: String,
    @Json(name = "task")
    val task: String
)