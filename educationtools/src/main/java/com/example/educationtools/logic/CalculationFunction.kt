package com.example.educationtools.logic

class NumberCalculationFunction(
    override var parameters: List<Parameter> = listOf(
        Parameter("Number", "first"),
        Parameter("Number", "second")
    ), override var type: String = "Number", val action: (Number, Number) -> Number
) : Function() {
    override fun run(): Any {
        return action(variables["first"] as Number, variables["second"] as Number)
    }

    companion object {
        fun sumFunction(): NumberCalculationFunction {
            return NumberCalculationFunction { x, y ->
                if (x is Float && y is Float) {
                    return@NumberCalculationFunction x + y
                }
                if (x is Int && y is Int) {
                    return@NumberCalculationFunction x + y
                }
                if (x is Int && y is Float) {
                    return@NumberCalculationFunction x + y
                }
                if (x is Float && y is Int) {
                    return@NumberCalculationFunction x + y
                }

                return@NumberCalculationFunction 3
            }
        }

        fun castNumber() {}
    }
}
