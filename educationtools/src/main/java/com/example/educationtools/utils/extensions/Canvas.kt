package com.example.educationtools.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.text.LineBreaker
import android.text.*
import androidx.core.graphics.withClip
import androidx.core.graphics.withTranslation

fun Canvas.drawBlockText(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    rect: RectF,
    start: Int = 0,
    end: Int = text.length,
    horizontalAlignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    verticalAlignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    textDir: TextDirectionHeuristic = TextDirectionHeuristics.LTR,
    spacingMult: Float = 1f,
    spacingAdd: Float = 0f
) {
    val staticLayout = StaticLayout.Builder
        .obtain(text, start, end, textPaint, width)
        .setAlignment(horizontalAlignment)
        .setTextDirection(textDir)
        .setLineSpacing(spacingAdd, spacingMult)
        .setBreakStrategy(LineBreaker.BREAK_STRATEGY_SIMPLE)
        .build()

    val topBaseline = when(verticalAlignment) {
        Layout.Alignment.ALIGN_NORMAL -> rect.top + textPaint.baselineShift
        Layout.Alignment.ALIGN_CENTER -> (rect.centerY() - staticLayout.height/2).coerceAtLeast(rect.top) + textPaint.baselineShift
        Layout.Alignment.ALIGN_OPPOSITE -> (rect.bottom - staticLayout.height).coerceAtLeast(rect.top) + textPaint.baselineShift
    }

    withClip(rect) {
        withTranslation(rect.left, topBaseline) {
            staticLayout.draw(this)
        }
    }
}

fun Canvas.drawTextAboutSide(
    text: CharSequence,
    textPaint: TextPaint,
    left: Float? = null,
    right: Float? = null,
    top: Float? = null,
    bottom: Float? = null
) {
    val textBounds = Rect()
    textPaint.getTextBounds(text.toString(), 0, text.length, textBounds)

    val xPos = left?.let {
        left
    } ?: right?.let {
        right - textBounds.width()
    } ?: 0f
    val yPos = bottom?.let {
        bottom
    } ?: top?.let {
        top + textBounds.height()
    } ?: 0f

    drawText(text.toString(), xPos, yPos, textPaint)
}