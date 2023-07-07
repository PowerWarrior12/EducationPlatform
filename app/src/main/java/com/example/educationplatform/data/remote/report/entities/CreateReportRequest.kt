package com.example.educationplatform.data.remote.report.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateReportRequest(
    @Json(name = "text")
    val text: String,
    @Json(name = "rating")
    val rating: Int
)
