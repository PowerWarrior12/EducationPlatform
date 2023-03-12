package com.example.educationtools.menu

import android.graphics.Canvas
import android.graphics.Point
import android.view.View
import androidx.core.content.res.ResourcesCompat

class MaskShadowBuilder(view: View, iconId: Int): View.DragShadowBuilder(view) {
    private val shadow = ResourcesCompat.getDrawable(view.context.resources, iconId, view.context.theme)

    override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
        val width: Int = view.width

        val height: Int = view.height

        shadow?.setBounds(0, 0, width, height)

        outShadowSize?.set(width, height)

        outShadowTouchPoint?.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas) {
        shadow?.draw(canvas)
    }
}