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
    private val leftKnot = Knot(this, Knot.Side.LEFT, false, 20f)
    private val rightKnot = Knot(this, Knot.Side.RIGHT, false, 20f)
    private val topKnot = Knot(this, Knot.Side.TOP, false, 20f)
    private val bottomKnot = Knot(this, Knot.Side.BOTTOM, true, 20f, onKnotConnected = ::connect)


    //Paints
    val mainPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    override val logicBlock: LogicBlock
        get() = calculationBlock
    override val inputKnots: List<Knot>
        get() = listOf(leftKnot, rightKnot, topKnot)
    override val outputKnots: List<Knot>
        get() = listOf(bottomKnot)

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

    private fun connect(knot: Knot) {
        calculationBlock.setNextBlock(knot.logicBlockView.logicBlock)
    }

    private fun updateKnotsPosition() {
        leftKnot.updatePosition(mainRect.left, mainRect.centerY())
        rightKnot.updatePosition(mainRect.right, mainRect.centerY())
        topKnot.updatePosition(mainRect.centerX(), mainRect.top)
        bottomKnot.updatePosition(mainRect.centerX(), mainRect.bottom)
    }
}