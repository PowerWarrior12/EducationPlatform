package com.example.educationtools.connection

import android.graphics.*
import com.example.educationtools.blocks.Knot

class ConnectionLine(val startKnot: Knot, val endKnot: Knot) {

    private val linePaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    fun draw(canvas: Canvas) {
        canvas.apply {
            drawLine(startKnot.getX(), startKnot.getY(), endKnot.getX(), endKnot.getY(), linePaint)
        }
    }
}