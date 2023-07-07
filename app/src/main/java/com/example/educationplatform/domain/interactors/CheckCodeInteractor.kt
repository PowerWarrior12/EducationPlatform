package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.CheckCodeData
import com.example.educationplatform.domain.entities.CheckResult
import com.example.educationplatform.domain.entities.CodeForExecution
import com.example.educationplatform.domain.repositories.CodeExecutionRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CheckCodeInteractor @Inject constructor(
    private val codeExecutionRepository: CodeExecutionRepository
) {
    suspend operator fun invoke(checkCodeData: CheckCodeData): CheckResult = coroutineScope {
        val testsAsync =
            checkCodeData.testDataList.map { testData ->
                async {
                    codeExecutionRepository.executeCode(
                        CodeForExecution(
                            checkCodeData.code, testData.first, checkCodeData.language
                        )
                    )
                }
            }
        var checkCodeResult: CheckResult? = null
        testsAsync.forEachIndexed { index, deferred ->
            val result = deferred.await()

            result.onSuccess { codeExecutionResult ->
                checkCodeResult =
                    if (codeExecutionResult.output == checkCodeData.testDataList[index].second) {
                        val message =
                            "Execution success\nYour output: ${codeExecutionResult.output}"
                        CheckResult(message, true)
                    } else {
                        val message =
                            "Execution error\n\nIn Test #${index + 1}\nInput: ${checkCodeData.testDataList[index].first}\nCorrect output: ${checkCodeData.testDataList[index].second}\nYour output: ${codeExecutionResult.output}\n\nPlease try again"
                        CheckResult(message, false)
                    }
            }.onFailure { error ->
                val message =
                    "${error.message}"
                checkCodeResult = CheckResult(message, false)
            }
            if (!checkCodeResult!!.status) {
                return@coroutineScope checkCodeResult!!
            }
        }
        return@coroutineScope checkCodeResult!!
    }
}
