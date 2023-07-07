package com.example.educationplatform.data.remote.codeExecution.entities

import com.squareup.moshi.Json

data class CodeExecutionErrorResponse(
    @Json(name = "Parameter")
    val parameter: String,
    @Json(name = "error")
    val message: String,
    @Json(name = "statusCode")
    val statusCode: String,
)