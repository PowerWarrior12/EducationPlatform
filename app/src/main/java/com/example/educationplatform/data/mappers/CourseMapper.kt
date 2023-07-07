package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.CreateCourseRequest
import com.example.educationplatform.data.remote.course.entities.EditCourseRequest
import com.example.educationplatform.domain.entities.Course

fun Course.toCreateCourseRequest(): CreateCourseRequest {
    return CreateCourseRequest(
        title = title,
        info = info,
        usersCount = usersCount,
        rating = rating,
        imageUrl = imageUrl,
        direction = direction,
        creatorId = creatorId
    )
}

fun Course.toEditCourseRequest(): EditCourseRequest {
    return EditCourseRequest(
        title = title,
        info = info,
        imageUrl = imageUrl,
        direction = direction
    )
}