package com.example.educationtools.logic

import android.util.Log

class EndBlock(private val memoryModel: MemoryModel): LogicBlock() {

    init {
        memoryModel.declareVarBlock(id)
    }

    var closeVars = mutableListOf<String>()

    override fun work() {
        closeVars.forEach {
            Log.d("MyTag", memoryModel.getVariable(it).value.toString())
        }
    }

    fun setCloseVars(variable: List<String>) {
        closeVars = variable as MutableList<String>
    }
}