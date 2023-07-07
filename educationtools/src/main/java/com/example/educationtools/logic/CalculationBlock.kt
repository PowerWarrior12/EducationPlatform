package com.example.educationtools.logic

import com.example.educationtools.logic.functions.Function
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import java.util.*

class CalculationBlock(id: String = UUID.randomUUID().toString()): LogicBlock(id) {
    private var nextBlock: LogicBlock? = null
    private var function: Function? = null
    private var changeableVar: Variable? = null

    override fun workOrThrow() {
        if (changeableVar != null && function != null) {
            val funcResult = function!!.run()
            if (changeableVar?.name != "!") {
                changeableVar!!.value = funcResult
                memoryModel.updateVariable(changeableVar!!)
            }
        } else {
            throw Exception(SYNTAX_ERROR_TEXT)
        }
        nextBlock?.workOrThrow() ?: throw Exception(CONNECTION_ERROR_MESSAGE)
    }

    override fun init() {
        memoryModel.declareVarBlock(id)
    }

    fun setFunctionAndVar(function: Function, variable: String): Boolean {
        deleteVarInMemory()

        //Получаем все доступные для данного блока переменные
        val declaredVars = memoryModel.getAvailableVariablesOrThrow(id)
        //Проверяем, была ли эта переменная уже объявлена
        if (variable in declaredVars) {
            val currentVar = memoryModel.getVariable(variable)
            if (currentVar.type != function.type) return false
        } else {
            memoryModel.declareVariable(id, Variable(variable, function.type))
        }
        this.changeableVar = Variable(variable, function.type)
        this.function = function
        return true
    }

    fun deleteFunctionAndVar() {
        deleteVarInMemory()
        changeableVar = null
        function = null
    }

    private fun deleteVarInMemory() {
        //Смотрим, содержалась ли в данном блоке переменная, и если да, была ли она объявлена в этом блоке
        //Если так, то удаляем её
        changeableVar?.let { currentVar ->
            memoryModel.getBlockVariables(id).onEach { variableName ->
                if (currentVar.name == variableName) {
                    memoryModel.deleteVariable(id, currentVar)
                }
            }
        }
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
}