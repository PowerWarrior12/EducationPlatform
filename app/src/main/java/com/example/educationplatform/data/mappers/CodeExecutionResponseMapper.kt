package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.codeExecution.entities.CodeExecutionResponse
import com.example.educationplatform.domain.entities.CodeExecutionResult

fun CodeExecutionResponse.toCodeExecutionResult(): CodeExecutionResult {
    return CodeExecutionResult(
        parameter = parameter,
        output = output,
        statusCode = statusCode,
        memory = memory,
        cpuTime = cpuTime,
        compilationStatus = compilationStatus
    )
}