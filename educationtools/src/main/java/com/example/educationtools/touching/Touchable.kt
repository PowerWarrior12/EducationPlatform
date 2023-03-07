package com.example.educationtools.touching

import android.graphics.PointF

interface Touchable {
    fun checkPointAvailability(x: Float, y: Float): Boolean
    fun checkPointAvailability(point: PointF): Boolean
    fun checkAndTouch(x: Float, y: Float)
    fun getPriority(): Int
    val blockScroll: Boolean
}