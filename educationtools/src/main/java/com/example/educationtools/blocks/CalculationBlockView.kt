package com.example.educationtools.blocks

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.example.educationtools.logic.CalculationBlock
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.MemoryModel

class CalculationBlockView: LogicBlockView() {

    private val calculationBlock = CalculationBlock()
    private val leftKnot = Knot(false, 20f, this, Knot.Side.LEFT)
    private val rightKnot = Knot(false, 20f, this, Knot.Side.RIGHT)
    private val topKnot = Knot(false, 20f, this, Knot.Side.TOP)
    private val bottomKnot = Knot(true, 20f, this, Knot.Side.BOTTOM)


    //Paints
    val mainPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    override var logicBlock: LogicBlock
        get() = calculationBlock
        set(value) {}
    override var inputKnots: List<Knot>
        get() = listOf(leftKnot, rightKnot, topKnot)
        set(value) {}
    override var outputKnots: List<Knot>
        get() = listOf(bottomKnot)
        set(value) {}

    override fun drawBorder(canvas: Canvas) {
        canvas.apply {
            drawRect(mainRect, mainPaint)
        }
    }

    override fun updatePosition(newPosition: PointF) {
        super.updatePosition(newPosition)
        updateKnotsPosition()
    }

    override fun updateSize(newWidth: Float, newHeight: Float) {
        super.updateSize(newWidth, newHeight)
        updateKnotsPosition()
    }

    override fun checkPointAvailability(x: Float, y: Float): Boolean {
        return mainRect.contains(x, y)
    }

    override fun checkPointAvailability(point: PointF): Boolean {
        return checkPointAvailability(point.x, point.y)
    }

    override fun checkAndTouch(x: Float, y: Float) {
    }

    override fun getPriority(): Int {
        return 2
    }

    private fun updateKnotsPosition() {
        leftKnot.updatePosition(mainRect.left, mainRect.centerY())
        rightKnot.updatePosition(mainRect.right, mainRect.centerY())
        topKnot.updatePosition(mainRect.centerX(), mainRect.top)
        bottomKnot.updatePosition(mainRect.centerX(), mainRect.bottom)
    }
}