package com.example.educationtools.connection

import android.graphics.*
import com.example.educationtools.blocks.Knot

class ConnectionLineShadow(val startKnot: Knot) {

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
            drawLine(startKnot.getX(), startKnot.getY(), targetPoint.x, targetPoint.y, linePaint)
        }
    }

    fun updateTargetPoint(xPos: Float, yPos: Float) {
        targetPoint.apply {
            x = xPos
            y = yPos
        }
    }
}