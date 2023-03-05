package com.example.educationtools.logic.functions

import com.example.educationtools.logic.MemoryModel
import kotlin.reflect.KClass

class VariableFunction(private val memoryModel: MemoryModel, private val variableName: String): Function() {
    override var parameters: List<Parameter>
        get() = emptyList()
        set(value) {}
    override var type: KClass<*>
        get() = memoryModel.getVariable(variableName).type
        set(value) {}

    override fun run(): Any? {
        return memoryModel.getVariable(variableName).value
    }
}