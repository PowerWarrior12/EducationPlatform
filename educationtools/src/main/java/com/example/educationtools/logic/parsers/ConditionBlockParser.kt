package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.logic.functions.Function
import com.example.educationtools.logic.functions.ReflectFunction
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import java.util.*

class ConditionBlockParser(private val blockId: String) {
    private val operands: List<String> = listOf("+", "-", "*", "/", "==", "!=", "<=", ">=", "||", "&&", ">", "<")
    private val operationPriority: Map<String, Int> = mapOf(
        "(" to 0,
        "||" to 1,
        "&&" to 2,
        ">" to 3,
        "<" to 3,
        "==" to 3,
        "!=" to 3,
        "<=" to 3,
        ">=" to 3,
        "-" to 4,
        "+" to 4,
        "*" to 5,
        "/" to 5
    )
    private val postfixParser = PostfixExpressionParser(operands = operands, operationPriority = operationPriority, functions = emptyList())

    fun parseOrThrow(textInput: String, memoryModel: MemoryModel): Function {
        val availableVars = memoryModel.getAvailableVariablesOrThrow(blockId)
        return parseFunction(textInput, availableVars, memoryModel, postfixParser)
    }
}