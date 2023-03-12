package com.example.educationtools.blocks

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import com.example.educationtools.connection.Knot
import com.example.educationtools.logic.CalculationBlock
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.parsers.CalculationBlockParser
import kotlin.system.measureTimeMillis

class CalculationBlockView: LogicBlockView() {

    private val calculationBlock = CalculationBlock()
    private val parser = CalculationBlockParser(calculationBlock.id)
    private val leftKnot = Knot(this, Knot.Side.LEFT, false, 20f)
    private val rightKnot = Knot(this, Knot.Side.RIGHT, false, 20f)
    private val topKnot = Knot(this, Knot.Side.TOP, false, 20f)
    private val bottomKnot = Knot(this, Knot.Side.BOTTOM, true, 20f, onKnotConnected = ::connect)


    //Paints
    private val mainPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    private val fillPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    override val logicBlock: LogicBlock
        get() = calculationBlock
    override val inputKnots: List<Knot>
        get() = listOf(leftKnot, rightKnot, topKnot)
    override val outputKnots: List<Knot>
        get() = listOf(bottomKnot)

    override fun drawBorder(canvas: Canvas) {
        canvas.apply {
            drawRect(mainRect, fillPaint)
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

    override fun setText(newText: String) {
        super.setText(newText)
        if (newText != "") {
            isError = try {
                val (variable, function) = parser.parseOrThrow(newText, memoryModel)
                calculationBlock.setFunctionAndVar(function, variable.name)
                false
            } catch (e: java.lang.Exception) {
                calculationBlock.deleteFunctionAndVar()
                true
            }
        }
        invalidate()
    }
}