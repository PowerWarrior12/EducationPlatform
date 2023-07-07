package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.course.entities.CreateModuleRequest
import com.example.educationplatform.data.remote.course.entities.EditModuleRequest
import com.example.educationplatform.domain.entities.Module

fun Module.toCreateModuleRequest(): CreateModuleRequest {
    return CreateModuleRequest(
        title = title,
        info = info,
        score = score
    )
}

fun Module.toEditModuleRequest(): EditModuleRequest {
    return EditModuleRequest(
        title = title,
        info = info,
        score = score
    )
}