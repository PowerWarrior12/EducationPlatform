package com.example.educationtools.logic

class CalculationBlock: LogicBlock() {
    var nextBlock: LogicBlock? = null
    var function: Function? = null
    var changeableVar: String? = null
    override fun start(inputVariables: List<Variable>) {
        inputVariables.forEach { variable ->
            variables[variable.name] = variable
        }
    }

    override fun work() {
        changeableVar?.let { changeableVar ->
            function
        }
    }
}