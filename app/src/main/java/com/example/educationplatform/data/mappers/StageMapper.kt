package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.CreateStageRequest
import com.example.educationplatform.data.remote.course.entities.EditStageRequest
import com.example.educationplatform.domain.entities.Stage

fun Stage.toCreateStageRequest(): CreateStageRequest {
    return CreateStageRequest(
        title = title,
        info = info,
        data = data,
        type = type,
        score = score
    )
}

fun Stage.toEditStageRequest(): EditStageRequest {
    return EditStageRequest(
        title = title,
        info = info,
        data = data,
        score = score
    )
}