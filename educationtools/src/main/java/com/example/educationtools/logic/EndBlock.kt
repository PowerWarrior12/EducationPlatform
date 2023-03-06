package com.example.educationtools.logic

import android.util.Log

class EndBlock(): LogicBlock() {

    private var variables = mutableListOf<String>()
    private val onWorkEndListeners = mutableListOf<(List<Variable>) -> Unit>()
    override fun work() {
        val result = variables.map {
            memoryModel.getVariable(it)
        }
        onWorkEndListeners.forEach {
            it(result)
        }
    }

    fun addOnEndListener(listener: (List<Variable>) -> Unit) {
        onWorkEndListeners.add(listener)
    }

    override fun init() {
        memoryModel.declareVarBlock(id)
    }

    fun setVariables(variable: List<String>) {
        variables = variable as MutableList<String>
    }
}