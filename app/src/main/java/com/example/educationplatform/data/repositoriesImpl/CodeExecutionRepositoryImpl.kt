package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.data.mappers.toCodeExecutionRequest
import com.example.educationplatform.data.mappers.toCodeExecutionResult
import com.example.educationplatform.data.remote.codeExecution.CodeExecutionApi
import com.example.educationplatform.domain.entities.CodeExecutionResult
import com.example.educationplatform.domain.entities.CodeForExecution
import com.example.educationplatform.domain.repositories.CodeExecutionRepository
import javax.inject.Inject

class CodeExecutionRepositoryImpl @Inject constructor(
    private val codeExecutionApi: CodeExecutionApi
): CodeExecutionRepository {
    override suspend fun executeCode(codeForExecution: CodeForExecution): Result<CodeExecutionResult> {
        val result = codeExecutionApi.execute(codeForExecution.toCodeExecutionRequest())
        return if (result.isSuccessful) {
            Result.success(result.body()!!.toCodeExecutionResult())
        } else {
            Result.failure(java.lang.Exception(result.errorBody()!!.string()))
        }
    }
}