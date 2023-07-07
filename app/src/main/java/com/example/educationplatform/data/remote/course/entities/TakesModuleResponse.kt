package com.example.educationplatform.data.remote.course.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TakesModuleResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "info")
    val info: String,
    @Json(name = "score")
    val score: Int,
    @Json(name = "courceId")
    val courseId: Int,
    @Json(name = "stages")
    val stages: List<TakesStageResponse>
)