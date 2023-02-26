package com.example.educationtools.logic

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions

class NumberCalculationFunction(
    override var parameters: List<Parameter> = listOf(
        Parameter(Number::class, "first"),
        Parameter(Number::class, "second")
    ), override var type: KClass<*> = Number::class, val action: (Number, Number) -> Number
) : Function() {
    override fun run(): Any {
        return action(variables["first"]?.run() as Number, variables["second"]?.run() as Number)
    }

    companion object {
        fun sumFunction(sign: Int = 1): NumberCalculationFunction {
            return NumberCalculationFunction { x, y ->
                if (x is Float && y is Float) {
                    return@NumberCalculationFunction x + sign * y
                }
                if (x is Int && y is Int) {
                    return@NumberCalculationFunction x + sign * y
                }
                if (x is Int && y is Float) {
                    return@NumberCalculationFunction x + sign * y
                }
                if (x is Float && y is Int) {
                    return@NumberCalculationFunction x + sign * y
                }

                return@NumberCalculationFunction (x as Float + sign * y as Float)
            }
        }

        fun subtractionFunction(): NumberCalculationFunction {
            return sumFunction(-1)
        }

        fun compositionFunction(): NumberCalculationFunction {

        }
    }
}
