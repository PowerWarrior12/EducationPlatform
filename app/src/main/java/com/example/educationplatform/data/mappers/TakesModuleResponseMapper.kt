package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.TakesModuleResponse
import com.example.educationplatform.domain.entities.TakesModule

fun TakesModuleResponse.toTakesModule(): TakesModule {
    val newStages = stages.map { takesStageResponse ->
        takesStageResponse.toTakesStage(id)
    }.toMutableList()
    return TakesModule(
        id = id,
        title = title,
        info = info,
        totalScore = newStages.fold(0) {acc, stage ->
            acc + stage.totalScore
        },
        courseId = courseId,
        userScore = newStages.fold(0) {acc, stage ->
            acc + stage.userScore
        },
        stages = newStages
    )
}