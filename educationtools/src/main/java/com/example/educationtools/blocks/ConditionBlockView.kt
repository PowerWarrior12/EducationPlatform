package com.example.educationtools.blocks

import android.graphics.*
import androidx.core.graphics.toRegion
import com.example.educationtools.connection.Knot
import com.example.educationtools.logic.CalculationBlock
import com.example.educationtools.logic.ConditionBlock
import com.example.educationtools.logic.LogicBlock

class ConditionBlockView: LogicBlockView() {
    private val conditionBlock = ConditionBlock()
    private val falseKnot = Knot(this, Knot.Side.LEFT, true, 20f, false, ::falseConnect)
    private val trueKnot = Knot(this, Knot.Side.RIGHT, true, 20f, true, ::trueConnect)
    private val topKnot = Knot(this, Knot.Side.TOP, false, 20f)
    private val bottomKnot = Knot(this, Knot.Side.BOTTOM, false, 20f)

    override val logicBlock: LogicBlock
        get() = conditionBlock
    override val inputKnots: List<Knot>
        get() = listOf(topKnot, bottomKnot)
    override val outputKnots: List<Knot>
        get() = listOf(falseKnot, trueKnot)

    //Paints
    private val mainPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    private val mainPath = Path().apply {
        moveTo(mainRect.centerX(), mainRect.top)
        lineTo(mainRect.right, mainRect.centerY())
        lineTo(mainRect.centerX(), mainRect.bottom)
        lineTo(mainRect.left, mainRect.centerY())
        close()
    }

    private var mainRegion = Region().apply {
        setPath(mainPath, mainRect.toRegion())
    }

    override fun drawBorder(canvas: Canvas) {
        canvas.apply {
            drawPath(mainPath, mainPaint)
        }
    }

    override fun checkPointAvailability(x: Float, y: Float): Boolean {
        return mainRegion.contains(x.toInt(), y.toInt())
    }

    override fun checkPointAvailability(point: PointF): Boolean {
        return checkPointAvailability(point.x, point.y)
    }

    override fun checkAndTouch(x: Float, y: Float) {
        TODO("Not yet implemented")
    }

    override fun updatePosition(newPosition: PointF) {
        super.updatePosition(newPosition)
        updateProperties()
    }

    override fun updateSize(newWidth: Float, newHeight: Float) {
        super.updateSize(newWidth, newHeight)
        updateProperties()
    }

    private fun trueConnect(knot: Knot) {
        conditionBlock.setTrueBlock(knot.logicBlockView.logicBlock)
    }

    private fun falseConnect(knot: Knot) {
        conditionBlock.setFalseBlock(knot.logicBlockView.logicBlock)
    }

    private fun updateProperties() {
        mainPath.apply {
            reset()
            moveTo(mainRect.centerX(), mainRect.top)
            lineTo(mainRect.right, mainRect.centerY())
            lineTo(mainRect.centerX(), mainRect.bottom)
            lineTo(mainRect.left, mainRect.centerY())
            close()
        }

        mainRegion = Region().apply {
            setPath(mainPath, mainRect.toRegion())
        }

        falseKnot.updatePosition(mainRect.left, mainRect.centerY())
        trueKnot.updatePosition(mainRect.right, mainRect.centerY())
        topKnot.updatePosition(mainRect.centerX(), mainRect.top)
        bottomKnot.updatePosition(mainRect.centerX(), mainRect.bottom)
    }
}