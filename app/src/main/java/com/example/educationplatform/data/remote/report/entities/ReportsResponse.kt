package com.example.educationplatform.data.remote.report.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReportsResponse (
    @Json(name = "reports")
    val reports: List<ReportResponse>
)