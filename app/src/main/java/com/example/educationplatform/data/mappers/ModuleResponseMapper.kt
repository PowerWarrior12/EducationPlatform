package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.ModuleResponse
import com.example.educationplatform.domain.entities.Module

fun ModuleResponse.toModule(): Module {
    return Module(
        id = id,
        title = title,
        info = info,
        score = score,
        courseId = courseId,
        stages = stages.map { stageResponse ->
            stageResponse.toStage(id)
        }
    )
}