package com.example.educationtools.logic

abstract class LogicBlock {
    var id: Int = -1
    val variables = mutableMapOf<String, Variable>()

    /**
     * Инициализация работы блока
     */
    abstract fun start(inputVariables: List<Variable>)

    /**
     * Выполнение работы блока и возвращение id следующего блока по списку
     */
    abstract fun work()
}