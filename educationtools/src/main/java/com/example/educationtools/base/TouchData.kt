package com.example.educationtools.base

import android.graphics.PointF

data class TouchData(
    var lastTouchId: Int = 0,
    val lastTouchPoint: PointF = PointF()
) {
    fun updateTouchData(id: Int, x: Float, y: Float) {
        lastTouchId = id
        lastTouchPoint.apply {
            this.x = x
            this.y = y
        }
    }
}
