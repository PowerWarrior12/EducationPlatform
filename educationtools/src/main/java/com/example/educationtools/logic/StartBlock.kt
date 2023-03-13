package com.example.educationtools.logic

import java.util.*

class StartBlock(id: String = UUID.randomUUID().toString()) : LogicBlock(id) {

    private var nextBlock: LogicBlock? = null
    private val startVariables = mutableListOf<Variable>()

    fun startOrThrow(inputVariables: List<Variable>) {
        if (startVariables.count() != inputVariables.count()) return
        startVariables.zip(inputVariables).forEach { pair ->
            if (pair.first.type != pair.second.type)
                throw java.lang.Exception("Типы параметра и введённого данного не совпадают: ${pair.first.name} and ${pair.second.value.toString()}")
            memoryModel.updateVariable(Variable(pair.first.name, pair.first.type, pair.second.value))
        }
        work()
    }

    override fun work() {
        nextBlock?.work()
    }

    override fun init() {
        memoryModel.declareVarBlock(id)
    }

    fun setNextBlock(newNextBlock: LogicBlock) {
        nextBlock = newNextBlock
        memoryModel.bindBlocksOrThrow(id, newNextBlock.id)
    }

    fun deleteNextBlock() {
        nextBlock = null
    }

    fun updateVariables(list: List<Variable>) {
        startVariables.forEach {
            memoryModel.deleteVariable(id, it)
        }
        startVariables.clear()
        startVariables.addAll(list)
        startVariables.forEach {
            memoryModel.declareVariable(id, it)
        }
    }
}