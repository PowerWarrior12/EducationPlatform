package com.example.educationtools.utils.extensions

import kotlin.math.max

fun Float.coerceOut(minValue: Float, maxValue: Float): Float {
    val center = minValue + (maxValue - minValue)/2
    if (this < minValue || this > maxValue) {
        return this
    }
    if (this - center < 0) return minValue
    return maxValue
}