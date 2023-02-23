package com.example.educationtools.utils

import android.graphics.Canvas
import android.graphics.text.LineBreaker
import android.text.*
import androidx.core.graphics.withTranslation

fun Canvas.drawMultilineText(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    x: Float,
    y: Float,
    start: Int = 0,
    end: Int = text.length,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    textDir: TextDirectionHeuristic = TextDirectionHeuristics.LTR,
    spacingMult: Float = 1f,
    spacingAdd: Float = 0f ) {

    val staticLayout = StaticLayout.Builder
        .obtain(text, start, end, textPaint, width)
        .setAlignment(alignment)
        .setTextDirection(textDir)
        .setLineSpacing(spacingAdd, spacingMult)
        .setBreakStrategy(LineBreaker.BREAK_STRATEGY_SIMPLE)
        .build()

    withTranslation(x, y) {
        staticLayout.draw(this)
    }
}