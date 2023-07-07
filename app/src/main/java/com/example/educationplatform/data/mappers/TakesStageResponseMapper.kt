package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.TakesStageResponse
import com.example.educationplatform.domain.entities.TakesStage

fun TakesStageResponse.toTakesStage(moduleId: Int): TakesStage {
    return TakesStage(
        id = id,
        title = title,
        info = info,
        data = data,
        type = type,
        totalScore = score,
        moduleId = moduleId,
        answer = stageResults.getOrNull(0)?.answer ?: "",
        isDone = stageResults.getOrNull(0)?.isDone?.toBoolean() ?: false,
        userScore = stageResults.getOrNull(0)?.userScore ?: 0,
    )
}