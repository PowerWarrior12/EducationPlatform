package com.example.educationtools.logic

import java.util.*

const val TYPE_ERROR_MESSAGE = "Типы параметра и введённого данного не совпадают: %s and %s"
const val CONNECTION_ERROR_MESSAGE = "Связи между блоками содержат ошибку"
const val PARAMETERS_COUNT_ERROR_MESSAGE = "Неверное число параметров"
class StartBlock(id: String = UUID.randomUUID().toString()) : LogicBlock(id) {

    private var nextBlock: LogicBlock? = null
    private val startVariables = mutableListOf<Variable>()

    fun startOrThrow(inputVariables: List<Variable>) {
        if (startVariables.count() != inputVariables.count()) throw Exception(
            PARAMETERS_COUNT_ERROR_MESSAGE)
        startVariables.zip(inputVariables).forEach { pair ->
            if (pair.first.type != pair.second.type)
                throw java.lang.Exception(TYPE_ERROR_MESSAGE.format(pair.first.name, pair.second.value.toString()))
            memoryModel.updateVariable(Variable(pair.first.name, pair.first.type, pair.second.value))
        }
        workOrThrow()
    }

    override fun workOrThrow() {
        nextBlock?.workOrThrow() ?: throw Exception(CONNECTION_ERROR_MESSAGE)
    }

    override fun init() {
        memoryModel.declareVarBlock(id)
    }

    fun setNextBlock(newNextBlock: LogicBlock) {
        nextBlock = newNextBlock
        memoryModel.bindBlocksOrThrow(id, newNextBlock.id)
    }

    fun deleteNextBlock() {
        nextBlock?.let { nextBlock ->
            memoryModel.deleteBlocksLinkOrThrow(id, nextBlock.id)
            this.nextBlock = null
        }
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