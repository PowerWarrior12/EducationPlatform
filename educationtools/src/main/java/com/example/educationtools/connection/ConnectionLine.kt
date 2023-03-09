package com.example.educationtools.connection

import android.graphics.*

class ConnectionLine(val startKnot: Knot, val endKnot: Knot): ConnectionLineBase() {

    init {
        updatePath(PointF(startKnot.getX(), startKnot.getY()), PointF(endKnot.getX(), endKnot.getY()), startKnot, endKnot)
        startKnot.addOnPositionChangedListener { x, y ->
            updatePath(PointF(x, y), PointF(endKnot.getX(), endKnot.getY()), startKnot, endKnot)
        }

        endKnot.addOnPositionChangedListener { x, y ->
            updatePath(PointF(startKnot.getX(), startKnot.getY()), PointF(x, y), startKnot, endKnot)
        }
    }

    private val linePaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 6f
        style = Paint.Style.STROKE
    }

    fun draw(canvas: Canvas) {
        canvas.apply {
            drawPath(path, linePaint)
        }
    }
}