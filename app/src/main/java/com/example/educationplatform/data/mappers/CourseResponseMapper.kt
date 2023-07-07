package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.CourseResponse
import com.example.educationplatform.domain.entities.Course

fun CourseResponse.toCourse(): Course {
    return Course(
        id = id,
        title = title,
        info = info,
        direction = direction,
        creatorId = -1,
        rating = rating,
        usersCount = usersCount ?: 0,
        imageUrl = imageUrl
    )
}