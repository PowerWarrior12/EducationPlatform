package com.example.educationtools.logic

import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import java.util.*

class EndBlock(id: String = UUID.randomUUID().toString()): LogicBlock(id) {

    private var variables = mutableListOf<String>()
    private val onWorkEndListeners = mutableListOf<(List<Variable>) -> Unit>()
    override fun workOrThrow() {
        val result = variables.map {
            memoryModel.getVariable(it)
        }
        if (result.isEmpty()) {
            throw Exception(SYNTAX_ERROR_TEXT)
        }
        onWorkEndListeners.forEach {
            it(result)
        }
    }

    fun addOnEndListener(listener: (List<Variable>) -> Unit) {
        onWorkEndListeners.clear()
        onWorkEndListeners.add(listener)
    }

    override fun init() {
        memoryModel.declareVarBlock(id)
    }

    fun setVariables(variable: List<String>) {
        variables = variable.toMutableList()
    }
}