package com.example.educationtools.logic.functions

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

open class ReflectFunction(private val func: KFunction<*>): Function() {

    private val _parameters: List<Parameter>

    init {
        _parameters = func.parameters.map { parameter ->
            Parameter(
                type = parameter.type.classifier as KClass<*>,
                name = parameter.name!!
            )
        }
    }

    override var parameters: List<Parameter>
        get() = _parameters
        set(value) {}
    override var type: KClass<*>
        get() = func.returnType.classifier as KClass<*>
        set(value) {}

    override fun run(): Any? {
        val varArray = variables.values.map { variable ->
            variable.run()
        }.toTypedArray()
        return func.call(*varArray)
    }
}