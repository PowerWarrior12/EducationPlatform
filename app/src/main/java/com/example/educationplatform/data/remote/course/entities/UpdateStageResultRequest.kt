package com.example.educationplatform.data.remote.course.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateStageResultRequest(
    @Json(name = "userScore")
    val score: Int,
    @Json(name = "isDone")
    val isDone: String,
    @Json(name = "answer")
    val answer: String,
    @Json(name = "stageId")
    val stageId: Int
)
