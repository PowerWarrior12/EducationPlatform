package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.StageResponse
import com.example.educationplatform.domain.entities.Stage

fun StageResponse.toStage(moduleId: Int): Stage {
    return Stage(
        id = id,
        title = title,
        info = info,
        data = data,
        type = type,
        score = score,
        moduleId = moduleId
    )
}