package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.SubscriptionCoursesResponse
import com.example.educationplatform.domain.entities.SubCourse

fun SubscriptionCoursesResponse.SubscriptionCourseResponse.toSubCourse(): SubCourse {
    return SubCourse(
        id = id,
        title = title,
        imageUrl = imageUrl,
        totalScore = totalScore,
        userScore = userScore
    )
}