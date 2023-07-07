package com.example.educationplatform.domain.repositories

import com.example.educationplatform.domain.entities.CodeExecutionResult
import com.example.educationplatform.domain.entities.CodeForExecution

interface CodeExecutionRepository {
    suspend fun executeCode(codeForExecution: CodeForExecution): Result<CodeExecutionResult>
}