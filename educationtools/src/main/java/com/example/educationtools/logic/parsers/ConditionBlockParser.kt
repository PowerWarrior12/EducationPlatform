package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.logic.functions.Function
import com.example.educationtools.logic.functions.ReflectFunction
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import java.util.*

class ConditionBlockParser(private val blockId: String) {

    private val postfixParser = PostfixExpressionParser()

    fun parseOrThrow(textInput: String, memoryModel: MemoryModel): Function {
        val text = textInput.replace(" ", "")
        val stack = Stack<Function>()
        val availableVars = memoryModel.getAvailableVariablesOrThrow(blockId)
        val elements = postfixParser.parseOrThrow(text)



        for (element in elements) {
            var function: Function? = null
            function = parseVariableFunction(element, availableVars, memoryModel)

            if (function != null) {
                stack.push(function)
                continue
            }

            function = parseTypeFunction(element)

            if (function != null) {
                stack.push(function)
                continue
            }

            if (conditionMethodsMap.containsKey(element)) {
                val fOverloads = conditionMethodsMap.getValue(element)
                if (stack.count() >= 2) {
                    val secondParam = stack.pop()
                    val firstParam = stack.pop()

                    val types = listOf(firstParam.type, secondParam.type)

                    if (fOverloads.containsKey(types)) {
                        function = ReflectFunction(fOverloads[types]!!)
                        function.setVariableOrThrow(firstParam)
                        function.setVariableOrThrow(secondParam)
                        stack.push(function)
                        continue
                    }
                } else {
                    throw Exception(SYNTAX_ERROR_TEXT)
                }
            }
            throw Exception(SYNTAX_ERROR_TEXT)

        }

        if (stack.count() > 1 || stack.isEmpty()) {
            throw Exception(SYNTAX_ERROR_TEXT)
        }
        val resultFunction = stack.pop()

        if (resultFunction.type != Boolean::class) {
            throw Exception(SYNTAX_ERROR_TEXT)
        }
        return resultFunction
    }

}