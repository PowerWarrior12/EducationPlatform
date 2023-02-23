package com.example.educationtools.blocks

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.example.educationtools.base.EditableBlock
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.base.EditableBlockFactory

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

    override fun checkPointConsists(point: PointF): Boolean {
        if (mainRect.contains(point.x, point.y)) {
            return true
        } else return false
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