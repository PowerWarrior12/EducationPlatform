package com.example.educationplatform.presentation.cource.module.blockDiagram

import com.example.educationplatform.domain.entities.CheckResult
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.logic.Variable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine

class BlockDiagramInspector {
    suspend fun inspect(blockDiagram: EditorViewBase, testDataList: List<Pair<String, String>>): CheckResult = coroutineScope {
        var checkResult: CheckResult? = null
        testDataList.forEachIndexed { index, testData ->
            val input = testData.first
            suspendCancellableCoroutine { continuation ->
                blockDiagram.addOnErrorListener {  error ->
                    checkResult = CheckResult("Execution error\n" +
                            "\n" +
                            "In Test #${index + 1}\n\n$error", false)
                    continuation.resumeWith(Result.success(Unit))
                }
                blockDiagram.addOnSuccessListener { variables ->
                    val resultOutput = variables.map { variable ->
                        variableToString(variable)
                    }.joinToString(separator = "\n")
                     checkResult = if (resultOutput == testDataList[index].second) {
                        val message =
                            "Execution success\n\nYour output: $resultOutput"
                        CheckResult(message, true)
                    } else {
                        val message =
                            "Execution error\n\nIn Test #${index + 1}\n\nInput: ${testDataList[index].first}\n\nCorrect output: ${testDataList[index].second}\n\nYour output: $resultOutput\n\nPlease try again"
                        CheckResult(message, false)
                    }
                    continuation.resumeWith(Result.success(Unit))
                }
                blockDiagram.start(input)
            }
            if (!checkResult!!.status) return@coroutineScope checkResult!!
        }

        return@coroutineScope checkResult!!
    }

    private fun variableToString(variable: Variable): String {
        return when (variable.type) {
            IntArray::class -> {
                (variable.value as IntArray).joinToString(" ")
            }
            FloatArray::class -> {
                (variable.value as FloatArray).joinToString(" ")
            }
            else -> {
                variable.value.toString()
            }
        }
    }
}