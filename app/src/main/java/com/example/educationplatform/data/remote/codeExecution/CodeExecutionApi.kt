package com.example.educationplatform.data.remote.codeExecution

import com.example.educationplatform.data.remote.codeExecution.entities.CodeExecutionRequest
import com.example.educationplatform.data.remote.codeExecution.entities.CodeExecutionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CodeExecutionApi {
    @POST("execute")
    suspend fun execute(@Body request: CodeExecutionRequest): Response<CodeExecutionResponse>
}