package com.example.educationtools.utils.extensions

import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sign

fun Float.coerceOut(minValue: Float, maxValue: Float): Float {
    val center = minValue + (maxValue - minValue)/2
    if (this < minValue || this > maxValue) {
        return this
    }
    if (this - center < 0) return minValue
    return maxValue
}

fun Float.roundToLesserValue(value: Float): Float {
    var steps = floor(this/value.toDouble()).absoluteValue
    if (sign > 0) {
        steps -= 1
    } else {
        steps += 1
    }
    val x = steps * value * sign
    return x.toFloat()
}

fun Float.roundToValue(value: Float): Float {
    var steps = (this/value).roundToInt().absoluteValue
    return steps * value * sign
}