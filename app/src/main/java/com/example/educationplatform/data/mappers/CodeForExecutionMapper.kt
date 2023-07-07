package com.example.educationplatform.data.mappers

import com.example.educationplatform.compilerId
import com.example.educationplatform.compilerSecret
import com.example.educationplatform.data.remote.codeExecution.entities.CodeExecutionRequest
import com.example.educationplatform.domain.entities.CodeForExecution

fun CodeForExecution.toCodeExecutionRequest(): CodeExecutionRequest {
    return CodeExecutionRequest(
        clientId = compilerId,
        clientSecret = compilerSecret,
        script = script,
        inputVars = inputVars,
        language = language
    )
}