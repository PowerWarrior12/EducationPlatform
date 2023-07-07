package com.example.educationplatform.data.remote.course.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserCoursesResponse(
    val courses: List<UserCourseResponse>
) {
    @JsonClass(generateAdapter = true)
    data class UserCourseResponse(
        @Json(name = "id")
        val id: Int,
        @Json(name = "title")
        val title: String,
        @Json(name = "imageUrl")
        val imageUrl: String,
    )
}