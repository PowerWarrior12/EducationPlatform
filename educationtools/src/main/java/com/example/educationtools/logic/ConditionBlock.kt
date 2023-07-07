package com.example.educationtools.logic

import com.example.educationtools.logic.functions.Function
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import java.util.*

class ConditionBlock(id: String = UUID.randomUUID().toString()): LogicBlock(id) {

    private var trueBlock: LogicBlock? = null
    private var falseBlock: LogicBlock? = null
    private var function: Function? = null
    override fun workOrThrow() {
        if (function != null) {
            val result = function!!.run() as Boolean
            if (result) {
                trueBlock?.workOrThrow() ?: throw Exception(CONNECTION_ERROR_MESSAGE)
            } else {
                falseBlock?.workOrThrow() ?: throw Exception(CONNECTION_ERROR_MESSAGE)
            }
        } else {
            throw Exception(SYNTAX_ERROR_TEXT)
        }
    }

    override fun init() {
        memoryModel.declareConditionBlock(id)
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
        trueBlock?.let { trueBlock ->
            memoryModel.deleteBlocksLinkOrThrow(id, trueBlock.id)
            this.trueBlock = null
        }
    }

    fun deleteFalseBlock() {
        falseBlock?.let { falseBlock ->
            memoryModel.deleteBlocksLinkOrThrow(id, falseBlock.id)
            this.falseBlock = null
        }
    }
}