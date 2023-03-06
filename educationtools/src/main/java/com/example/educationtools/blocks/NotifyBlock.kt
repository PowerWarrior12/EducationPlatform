package com.example.educationtools.blocks

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.example.educationtools.base.EditableBlock
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.base.EditableBlockFactory
import com.example.educationtools.selection.SimpleSelector

class NotifyBlock(): EditableBlockBase() {

    //Paints
    val mainPaint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    override fun drawFigure(canvas: Canvas) {
        canvas.apply {
            drawRect(mainRect, mainPaint)
        }
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


    data class EditableConfigurations(
        var centerX: Float,
        var centerY: Float,
        var width: Float = 0f,
        var height: Float = 0f,
        var text: String = ""
    ) : EditableBlockFactory<NotifyBlock> {
        override fun create(): EditableBlock {
            TODO("Not yet implemented")
        }
    }

}