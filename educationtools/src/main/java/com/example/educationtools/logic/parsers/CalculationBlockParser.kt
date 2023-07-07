package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.logic.Variable
import com.example.educationtools.logic.functions.Function
import com.example.educationtools.logic.functions.ReflectFunction
import com.example.educationtools.logic.functions.TypeFunction
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import com.example.educationtools.utils.TYPE_ERROR_TEXT
import com.example.educationtools.utils.extensions.split
import java.util.*

class CalculationBlockParser(private val blockId: String) {
    private val operands: List<String> = listOf("+", "-", "*", "/")
    private val operationPriority: Map<String, Int> = mapOf(
        "(" to 0,
        "-" to 1,
        "+" to 1,
        "*" to 2,
        "/" to 2
    )
    private val postfixParser = PostfixExpressionParser(operands = operands, operationPriority = operationPriority, functions = emptyList())
    fun parseOrThrow(text: String, memoryModel: MemoryModel): Pair<Variable, Function> {
        val operands = text.split('=')
        if (operands.count() != CALCULATION_OPERANDS_COUNT) throw Exception(SYNTAX_ERROR_TEXT)

        val availableVariables = memoryModel.getAvailableVariablesOrThrow(blockId)
        var function = parseFunction(operands[1], availableVariables, memoryModel, postfixParser)

        //setVariable
        val variableName = operands[0].trim()
        val variable = if (VARIABLE_TEMPLATE.matches(variableName)) {
            Variable(name = variableName, type = function.type)
        } else if (ARRAY_ELEMENT_TEMPLATE.matches(variableName)) {
            val arrayName = text.split('[')[0]
            val arrayFunction = parseVariableFunction(arrayName, availableVariables, memoryModel)
                ?: throw Exception(SYNTAX_ERROR_TEXT)

            val index = variableName.removePrefix("$arrayName[").removeSuffix("]")
            val indexFunction = parseFunction(index, availableVariables, memoryModel, postfixParser)

            val getArrayElementOverloads = arrayElementFunctions["set"]

            if (getArrayElementOverloads?.containsKey(indexFunction.type) != true) throw Exception(SYNTAX_ERROR_TEXT)

            val arrayV = ReflectFunction(getArrayElementOverloads.getValue(indexFunction.type))
            arrayV.setVariableOrThrow(arrayFunction)
            arrayV.setVariableOrThrow(indexFunction)
            arrayV.setVariableOrThrow(function)

            function = arrayV

            Variable(name = "!", type = function.type)
        } else {
            throw Exception(SYNTAX_ERROR_TEXT)
        }
        return Pair(variable, function)

    }

    private fun parseTypeFunction(text: String): Function? {
        if (INT_TEMPLATE.matches(text)) return TypeFunction.generateTypeFunction(text.toInt())
        if (FLOAT_TEMPLATE.matches(text)) return TypeFunction.generateTypeFunction(text.toFloat())
        return null
    }
}