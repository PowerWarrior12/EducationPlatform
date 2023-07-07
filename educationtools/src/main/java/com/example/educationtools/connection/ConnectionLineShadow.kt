package com.example.educationtools.connection

import android.graphics.*

private const val blockOffset = 80f

class ConnectionLineShadow(val startKnot: Knot, gridSize: Float): ConnectionLineBase(gridSize) {


    private val targetPoint = PointF()
    private var focusKnot: Knot? = null

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

        if (focusKnot == null) {
            updatePath(PointF(startKnot.getX(), startKnot.getY()), targetPoint, startKnot, focusKnot)
        } else {
            updatePath(PointF(startKnot.getX(), startKnot.getY()), PointF(focusKnot!!.getX(), focusKnot!!.getY()), startKnot, focusKnot)
        }
    }

    fun setFocus(knot: Knot) {
        focusKnot = knot
    }

    fun deleteFocus() {
        focusKnot = null
    }

    fun getFocusOrNull(): Knot? {
        return focusKnot
    }
}