package com.example.educationplatform.data.remote.codeExecution.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CodeExecutionResponse(
    @Json(name = "Parameter")
    val parameter: String?,
    @Json(name = "output")
    val output: String?,
    @Json(name = "statusCode")
    val statusCode: String?,
    @Json(name = "memory")
    val memory: String?,
    @Json(name = "cpuTime")
    val cpuTime: String?,
    @Json(name = "compilationStatus")
    val compilationStatus: String?
)
