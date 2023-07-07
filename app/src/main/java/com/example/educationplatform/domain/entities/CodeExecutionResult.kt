package com.example.educationplatform.domain.entities

data class CodeExecutionResult (
    val parameter: String?,
    val output: String?,
    val statusCode: String?,
    val memory: String?,
    val cpuTime: String?,
    val compilationStatus: String?
)