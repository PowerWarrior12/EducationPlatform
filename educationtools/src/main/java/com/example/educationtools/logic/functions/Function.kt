package com.example.educationtools.logic.functions

import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

abstract class Function {

    abstract var parameters: List<Parameter>

    abstract var type: KClass<*>

    abstract fun run(): Any?

    private var variablesCount: Int = 0

    protected val variables: MutableMap<String, Function> = mutableMapOf()

    fun setVariableOrThrow(variable: Function) {
        if (variablesCount >= parameters.count()) {
            throw java.lang.Exception("Invalid number of parameters")
        }
        val parameter = parameters[variablesCount]
        if (parameter.type !in variable.type.allSuperclasses && parameter.type != variable.type) {
            throw java.lang.Exception("Invalid type")
        }
        variables[parameter.name] = variable
        variablesCount++
    }

    class Parameter (
        val type: KClass<*>,
        val name: String
    )
}