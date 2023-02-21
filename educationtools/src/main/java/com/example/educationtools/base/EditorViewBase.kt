package com.example.educationtools.base

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation

class EditorViewBase @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //Данные о изменениях экрана
    private val transformations = Transformations()
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val gestureDetector = GestureDetector(context, ScrollListener())

    //Данные о тачах
    private val touchData = TouchData()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            withScale(
                x = transformations.scale,
                y = transformations.scale,
                pivotX = transformations.translation.x,
                pivotY = transformations.translation.y
            ) {
                withTranslation(
                    x = transformations.translation.x,
                    y = transformations.translation.y
                ) {
                    drawRect(Rect(10, 10, 300, 200), Paint().apply {
                        isAntiAlias = true
                        style = Paint.Style.FILL
                        color = Color.RED
                    })

                    drawRect(Rect(30, 250, 330, 450), Paint().apply {
                        isAntiAlias = true
                        style = Paint.Style.FILL
                        color = Color.RED
                    })
                }
            }
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        return processTouch(event)
    }

    /**
     * Обработка тачей
     */
    private fun processTouch(event: MotionEvent): Boolean {
        return if (event.pointerCount > 1) {
            scaleGestureDetector.onTouchEvent(event)
        } else {
            gestureDetector.onTouchEvent(event)
        }
    }

    /**
     * Класс для трансформаций, происходящих с рабочей областью View
     */
    private inner class Transformations() {
        val translation = PointF(0f, 0f)

        var scale: Float = 1f
            private set

        fun addTranslation(dx: Float, dy: Float) {
            translation.x = translation.x - dx
            translation.y = translation.y - dy
            invalidate()
        }

        fun addScale(scale: Float) {
            this.scale *= scale
            invalidate()
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            transformations.addScale(detector.scaleFactor)
            return true
        }
    }

    private inner class ScrollListener: GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.d("ScrollListener", "xVelocity = $velocityX yVelocity = $velocityY")
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            transformations.addTranslation(
                distanceX,
                distanceY
            )
            return true
        }
    }
}