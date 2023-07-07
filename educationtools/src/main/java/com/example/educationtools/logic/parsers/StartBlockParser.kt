package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.Variable
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import kotlin.reflect.KClass

class StartBlockParser {

    fun parseOrThrow(text: String): List<Variable> {
        val resultArray = mutableListOf<Variable>()
        val variableArray = text.split(',')
        variableArray.forEach {
            val prepareText = it.trim().replace(SPACES, " ")
            val attributes = prepareText.split(' ')
            if (attributes.count() != VARIABLE_ATTRIBUTES_COUNT) throw java.lang.Exception(SYNTAX_ERROR_TEXT)

            fun getX(): KClass<*> {
                return Array<Int>::class
            }

            //Разбираем аттрибут типа
            if (!types.contains(attributes[0])) throw java.lang.Exception(SYNTAX_ERROR_TEXT)
            //Разбираем аттрибут названия переменной
            if (!VARIABLE_TEMPLATE.matches(attributes[1])) throw java.lang.Exception(SYNTAX_ERROR_TEXT)

            resultArray.add(Variable(name = attributes[1], type = types.getValue(attributes[0])))
        }
        return resultArray
    }
}