package com.example.educationtools.logic

import kotlin.reflect.KClass

class TypeFunction(private val _type: KClass<*>, private val value: Any): Function() {
    override var parameters: List<Parameter>
        get() = emptyList()
        set(value) {}

    override var type: KClass<*>
        get() = _type
        set(value) {}

    override fun run(): Any {
        return value
    }

    companion object {
        fun generateTypeFunction(value: Any): TypeFunction {
            return TypeFunction(value::class, value)
        }
    }
}