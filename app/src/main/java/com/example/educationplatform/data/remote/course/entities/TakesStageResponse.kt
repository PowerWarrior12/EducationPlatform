package com.example.educationplatform.data.remote.course.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TakesStageResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "info")
    val info: String,
    @Json(name = "data")
    val data: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "score")
    val score: Int,
    @Json(name = "stageResultDtos")
    val stageResults: List<StageResultResponse>

)

@JsonClass(generateAdapter = true)
data class StageResultResponse(
    @Json(name = "score")
    val userScore: Int,
    @Json(name = "isDone")
    val isDone: String,
    @Json(name = "answer")
    val answer: String
)
