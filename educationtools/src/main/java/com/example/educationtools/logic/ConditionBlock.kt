package com.example.educationtools.logic

import android.util.Log
import com.example.educationtools.logic.functions.ConditionFunction
import com.example.educationtools.logic.functions.Function
import kotlin.reflect.KFunction

class ConditionBlock(private val memoryModel: MemoryModel): LogicBlock() {

    private var trueBlock: LogicBlock? = null
    private var falseBlock: LogicBlock? = null
    private var function: ConditionFunction? = null
    override fun work() {
        if (function != null) {
            val result = function!!.run() as Boolean
            if (result) {
                trueBlock?.work()
            } else {
                falseBlock?.work()
            }
        }
    }

    fun setFunction(newFunction: ConditionFunction) {
        function = newFunction
    }

    fun deleteFunction() {
        function = null
    }

    fun setTrueBlock(newTrueBlock: LogicBlock) {
        trueBlock = newTrueBlock
        memoryModel.bindBlocksOrThrow(id, newTrueBlock.id, true)
    }

    fun setFalseBlock(newFalseBlock: LogicBlock) {
        falseBlock = newFalseBlock
        memoryModel.bindBlocksOrThrow(id, newFalseBlock.id, false)
    }

    fun deleteTrueBlock() {
        trueBlock = null
    }

    fun deleteFalseBlock() {
        falseBlock = null
    }
}