package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.UserCoursesResponse
import com.example.educationplatform.domain.entities.UsersCourse

fun UserCoursesResponse.UserCourseResponse.toUsersCourse(): UsersCourse {
    return UsersCourse(
        id = id,
        title = title,
        imageUrl = imageUrl
    )
}