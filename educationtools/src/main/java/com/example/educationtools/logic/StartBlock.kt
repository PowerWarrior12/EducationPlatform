package com.example.educationtools.logic

class StartBlock: LogicBlock() {

    init {
        id = 0
    }

    var nextBlock: LogicBlock? = null
    private val startVariables = mutableListOf<String>()

    override fun start(inputVariables: List<Variable>) {
        if (inputVariables.count() != inputVariables.count()) return
        startVariables.zip(inputVariables).forEach { pair ->
            variables[pair.first] = pair.second.apply {
                name = pair.first
                id = this@StartBlock.id
            }
        }
    }

    override fun work() {
        nextBlock?.start(variables.values.toList())
        nextBlock?.work()
    }

    fun updateVariables(list: List<String>) {
        startVariables.clear()
        startVariables.addAll(list)
    }
}