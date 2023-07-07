package com.example.educationplatform.data.remote.course.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateCourseRequest(
    @Json(name = "title")
    val title: String,
    @Json(name = "info")
    val info: String,
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "usersCount")
    val usersCount: Int,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "direction")
    val direction: String,
    @Json(name = "creatorId")
    val creatorId: Int
)
