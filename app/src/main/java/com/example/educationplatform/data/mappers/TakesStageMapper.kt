package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.UpdateStageResultRequest
import com.example.educationplatform.domain.entities.TakesStage

fun TakesStage.toUpdateStageResultRequest(): UpdateStageResultRequest {
    return UpdateStageResultRequest(
        score = userScore,
        isDone = isDone.toString(),
        answer = answer,
        stageId = id
    )
}