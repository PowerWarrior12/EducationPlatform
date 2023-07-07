package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.Variable
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT

class InputVariablesParser {
    fun parseOrThrow(text: String): List<Variable> {
        val resultArray = mutableListOf<Variable>()
        val variableArray = text.split("\n")
        variableArray.forEach { it ->
            val prepareText = it.trim()

            if (INT_TEMPLATE.matches(prepareText)) {
                resultArray.add(Variable(type = Int::class, value = prepareText.toInt()))
            } else if (FLOAT_TEMPLATE.matches(prepareText))  {
                resultArray.add(Variable(type = Float::class, value = prepareText.toFloat()))
            } else if (INTEGER_ARRAY_TEMPLATE.matches(prepareText)) {
                resultArray.add(Variable(type = IntArray::class, value = prepareText.split(" ").map { numb ->
                    numb.toInt()
                }.toIntArray()))
            } else if (FLOAT_ARRAY_TEMPLATE.matches(prepareText)) {
                resultArray.add(Variable(type = FloatArray::class, value = prepareText.split(" ").map { numb ->
                    numb.toFloat()
                }.toFloatArray()))
            } else {
                throw Exception(SYNTAX_ERROR_TEXT)
            }


        }
        return resultArray
    }
}