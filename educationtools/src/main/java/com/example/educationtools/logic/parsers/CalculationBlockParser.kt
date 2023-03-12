package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.logic.Variable
import com.example.educationtools.logic.functions.Function
import com.example.educationtools.logic.functions.ReflectFunction
import com.example.educationtools.logic.functions.TypeFunction
import com.example.educationtools.logic.functions.VariableFunction
import com.example.educationtools.utils.extensions.split

class CalculationBlockParser(private val blockId: String) {

    fun parseOrThrow(text: String, memoryModel: MemoryModel): Pair<Variable, Function> {
        val operands = text.split('=')
        if (operands.count() != CALCULATION_OPERANDS_COUNT) throw Exception(SYNTAX_ERROR_TEXT)

        val function = parseFunction(operands[1], memoryModel.getAvailableVariablesOrThrow(blockId), memoryModel)
        val variable = operands[0].trim()
        if (!VARIABLE_TEMPLATE.matches(variable)) throw Exception(SYNTAX_ERROR_TEXT)
        return Pair(Variable(name = variable, type = function.type), function)
    }

    private fun parseFunction(text: String, availableVariables: List<String>, memoryModel: MemoryModel): Function {
        val functionText = text.trim()

        var function: Function? = parseTypeFunction(functionText)
        if (function != null) return function

        function = parseVariableFunction(functionText, availableVariables, memoryModel)
        if (function != null) return function

        if (!METHOD_TEMPLATE.matches(functionText)) throw Exception(SYNTAX_ERROR_TEXT)

        val functionName = functionText.split('(')[0]

        if (!calculationMethodsMap.contains(functionName)) throw Exception(SYNTAX_ERROR_TEXT)

        val functionOverloads = calculationMethodsMap.getValue(functionName)

        val parameters = functionText.removePrefix("$functionName(").removeSuffix(")")

        val paramFunctions = mutableListOf<Function>()

        parameters.split(getComaSeparatorIndexes(parameters)).forEach {
            paramFunctions.add(parseFunction(it, availableVariables, memoryModel))
        }

        val paramTypes = paramFunctions.map {
            it.type
        }

        if (!functionOverloads.containsKey(paramTypes)) throw Exception(TYPE_ERROR_TEXT)

        function = ReflectFunction(functionOverloads.getValue(paramTypes))
        paramFunctions.forEach {
            function.setVariableOrThrow(it)
        }
        return function
    }

    private fun parseTypeFunction(text: String): Function? {
        if (INT_TEMPLATE.matches(text)) return TypeFunction.generateTypeFunction(text.toInt())
        if (FLOAT_TEMPLATE.matches(text)) return TypeFunction.generateTypeFunction(text.toFloat())
        return null
    }
}