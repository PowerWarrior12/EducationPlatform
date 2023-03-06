package com.example.educationtools.logic

import java.util.UUID

abstract class LogicBlock() {
    private var _memoryModel: MemoryModel? = null
    val memoryModel
        get() = checkNotNull(_memoryModel)
    fun setMemoryModel(memoryModel: MemoryModel) {
        this._memoryModel = memoryModel
        init()
    }
    val id = UUID.randomUUID().toString()

    /**
     * Выполнение работы блока и возвращение id следующего блока по списку
     */
    abstract fun work()
    abstract fun init()
}