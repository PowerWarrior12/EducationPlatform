package com.example.educationtools.blocks

import android.graphics.*
import androidx.core.graphics.toRegion
import com.example.educationtools.connection.Knot
import com.example.educationtools.logic.EndBlock
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.StartBlock
import com.example.educationtools.logic.parsers.EndBlockParser

class EndBlockView: LogicBlockView() {
    private val endBlock = EndBlock()
    private val parser = EndBlockParser(endBlock.id)
    private val inputKnot = Knot(this, Knot.Side.TOP, false, 20f)

    override val logicBlock: LogicBlock
        get() = endBlock
    override val inputKnots: List<Knot>
        get() = listOf(inputKnot)
    override val outputKnots: List<Knot>
        get() = listOf()

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

    private val mainPath = Path().apply {
        addOval(mainRect, Path.Direction.CCW)
    }

    private var mainRegion = Region().apply {
        setPath(mainPath, mainRect.toRegion())
    }

    override fun drawBorder(canvas: Canvas) {
        canvas.apply {
            drawPath(mainPath, fillPaint)
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
    }

    override fun updatePosition(newPosition: PointF) {
        super.updatePosition(newPosition)
        updateParams()
    }

    override fun updateSize(newWidth: Float, newHeight: Float) {
        super.updateSize(newWidth, newHeight)
        updateParams()
    }

    override fun setText(newText: String) {
        super.setText(newText)
        if (newText != "") {
            try {
                val variables = parser.parseOrThrow(newText, memoryModel)
                endBlock.setVariables(variables)
                isError = false
            } catch (e: java.lang.Exception) {
                endBlock.setVariables(emptyList())
                isError = true
            }
        }
        invalidate()
    }

    private fun updateParams() {
        mainPath.apply {
            reset()
            addOval(mainRect, Path.Direction.CCW)
        }
        mainRegion = Region().apply {
            setPath(mainPath, mainRect.toRegion())
        }
        updateKnotsPosition()
    }

    private fun updateKnotsPosition() {
        inputKnot.updatePosition(mainRect.centerX(), mainRect.top)
    }
}