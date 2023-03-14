package com.example.educationtools.logic

abstract class LogicBlock(val id: String) {
    private var _memoryModel: MemoryModel? = null
    val memoryModel
        get() = checkNotNull(_memoryModel)
    fun setMemoryModel(memoryModel: MemoryModel) {
        this._memoryModel = memoryModel
        init()
    }


    /**
     * Выполнение работы блока и возвращение id следующего блока по списку
     */
    abstract fun workOrThrow()
    abstract fun init()
}