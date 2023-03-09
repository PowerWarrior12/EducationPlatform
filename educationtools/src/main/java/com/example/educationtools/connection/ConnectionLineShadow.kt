package com.example.educationtools.connection

import android.graphics.*
import com.example.educationtools.utils.extensions.coerceOut

private const val blockOffset = 80f

class ConnectionLineShadow(val startKnot: Knot): ConnectionLineBase() {


    private val targetPoint = PointF()
    private val linePaint = Paint().apply {
        isAntiAlias = true
        color = Color.GRAY
        strokeWidth = 3f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(30f, 10f), 0f)
    }

    fun draw(canvas: Canvas) {
        canvas.apply {
            drawPath(path, linePaint)
        }
    }

    fun updateTargetPoint(xPos: Float, yPos: Float) {
        targetPoint.apply {
            x = xPos
            y = yPos
        }

        updatePath(PointF(startKnot.getX(), startKnot.getY()), targetPoint, startKnot, null)
    }
}