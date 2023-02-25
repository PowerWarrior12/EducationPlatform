package com.example.educationtools.logic

abstract class Function {

    protected abstract var parameters: List<Parameter>

    abstract var type: String

    abstract fun run(): Any

    private var variablesCount: Int = 0

    protected val variables: MutableMap<String, Function> = mutableMapOf()

    fun setVariableOrThrow(variable: Function) {
        if (variablesCount >= parameters.count()) {
            throw java.lang.Exception("Invalid number of parameters")
        }
        val parameter = parameters[variablesCount]
        if (parameter.type != variable.type) {
            throw java.lang.Exception("Invalid type")
        }
        variables[parameter.name] = variable
        variablesCount++
    }

    class Parameter (
        val type: String,
        val name: String
    )
}