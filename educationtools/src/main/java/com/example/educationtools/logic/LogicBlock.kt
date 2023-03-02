package com.example.educationtools.logic

import java.util.UUID

abstract class LogicBlock {
    val id = UUID.randomUUID().toString()
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