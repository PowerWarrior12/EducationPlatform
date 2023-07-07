package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.CodeExecutionResult
import com.example.educationplatform.domain.entities.CodeForExecution
import com.example.educationplatform.domain.repositories.CodeExecutionRepository
import javax.inject.Inject

class CodeExecutionInteractor @Inject constructor(
    private val codeExecutionRepository: CodeExecutionRepository
) {
    suspend operator fun invoke(codeForExecution: CodeForExecution): Result<CodeExecutionResult> {
        return codeExecutionRepository.executeCode(codeForExecution)
    }
}