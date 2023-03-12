package com.example.educationtools.logic

import com.example.educationtools.logic.functions.Function

class DoWhileBlock: LogicBlock() {
    private var trueBlock: LogicBlock? = null
    private var falseBlock: LogicBlock? = null
    private var function: Function? = null

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

    override fun init() {
        memoryModel.declareDoWhileBlock(id)
    }

    fun setFunction(newFunction: Function) {
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