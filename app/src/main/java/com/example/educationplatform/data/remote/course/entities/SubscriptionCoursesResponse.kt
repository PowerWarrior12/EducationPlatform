package com.example.educationplatform.data.remote.course.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscriptionCoursesResponse(
    @Json(name = "courses")
    val courses: List<SubscriptionCourseResponse>
) {
    @JsonClass(generateAdapter = true)
    data class SubscriptionCourseResponse(
        @Json(name = "id")
        val id: Int,
        @Json(name = "title")
        val title: String,
        @Json(name = "imageUrl")
        val imageUrl: String,
        @Json(name = "totalScores")
        val totalScore: Int,
        @Json(name = "userScores")
        val userScore: Int
    )
}
