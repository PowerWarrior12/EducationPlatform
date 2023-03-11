package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.logic.functions.ConditionFunction
import com.example.educationtools.logic.functions.VariableFunction
import com.example.educationtools.logic.functions.Function
import java.util.*

class ConditionBlockParser(private val memoryModel: MemoryModel, private val blockId: String) {

    private val postfixParser = PostfixExpressionParser()

    /*fun parseOrThrow(text: String): ConditionFunction {
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



        }
    }*/

}