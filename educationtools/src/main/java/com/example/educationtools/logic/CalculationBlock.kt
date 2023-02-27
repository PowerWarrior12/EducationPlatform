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
        if (changeableVar != null && function != null) {
            val resultValue = function!!.run()
            val resultType = function!!.type
            if (variables.contains(changeableVar)) {
                if (variables[changeableVar!!]?.type != resultType) {
                    throw Exception("Different types of data")
                }
                variables[changeableVar!!]?.value = resultValue
            } else {
                variables[changeableVar!!] = Variable(id, changeableVar!!, resultType, resultValue)
            }
        }
        nextBlock?.start(variables.values.toList())
        nextBlock?.work()
    }
}