package com.example.educationtools.logic

import java.util.UUID

abstract class LogicBlock() {
    val id = UUID.randomUUID().toString()

    /**
     * Выполнение работы блока и возвращение id следующего блока по списку
     */
    abstract fun work()
}