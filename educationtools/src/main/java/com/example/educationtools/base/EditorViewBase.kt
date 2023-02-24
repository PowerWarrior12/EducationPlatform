package com.example.educationtools.base

import android.animation.PointFEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import com.example.educationtools.control.DragAndDropManager
import com.example.educationtools.control.SelectManager

const val SCROLL_VELOCITY_FACTOR = 0.005f
const val FLING_DURATION = 100L

class EditorViewBase @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ParentEditor {

    //Данные о изменениях экрана
    private val transformations = Transformations()
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val gestureDetector = GestureDetector(context, ScrollListener())

    //Дочерние блоки
    private val children: MutableList<EditableBlock> = mutableListOf()

    private val selectorManager = SelectManager(children.toMutableList())
    private val dadManager = DragAndDropManager()

    init {
        selectorManager.addLongPressListener(dadManager::drag)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            withTranslation(
                x = transformations.translation.x,
                y = transformations.translation.y
            ) {
                withScale(
                    x = transformations.scale,
                    y = transformations.scale,
                    pivotX = width/2 - transformations.translation.x,
                    pivotY = height/2 - transformations.translation.y
                ) {
                    Log.d(EditorViewBase::class.simpleName, "focusX: ${transformations.scalePivot.x} defaultFocusX: ${width/2 - transformations.translation.x}")
                    Log.d(EditorViewBase::class.simpleName, "focusY: ${transformations.scalePivot.y} defaultFocusY: ${height/2 - transformations.translation.y}")
                    children.forEach {
                        it.draw(this)
                    }
                }
            }
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        return processTouch(event)
    }

    fun addChild(child: EditableBlock) {
        children.add(child)
        child.setEditorParent(this)
        selectorManager.addEditable(child)
        invalidate()
    }

    /**
     * Обработка тачей
     */
    private fun processTouch(event: MotionEvent): Boolean {
        return if (event.pointerCount > 1) {
            scaleGestureDetector.onTouchEvent(event)
        } else {
            if (event.action == MotionEvent.ACTION_MOVE) {
                dadManager.dragProcess(event.x, event.y)
            } else if (event.action == MotionEvent.ACTION_BUTTON_RELEASE) {
                dadManager.drop()
            }
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

        val scalePivot = PointF(width/2f, height/2f)
        private val originScalePivot = PointF(width/2f, height/2f)

        fun addTranslation(dx: Float, dy: Float) {
            translation.x = translation.x - dx
            translation.y = translation.y - dy
            updateScalePivot()
            invalidate()
        }

        fun addScale(scale: Float) {
            this.scale *= scale
            invalidate()
        }

        fun convertPointToTransform(x: Float, y: Float): PointF {
            return PointF().apply {
                this.x = (width / 2 - translation.x) + ((x - width / 2) / scale)
                this.y = (height / 2 - translation.y) + ((y - height / 2 ) / scale)
            }
        }

        fun convertPointToTransform(pointF: PointF): PointF {
            return convertPointToTransform(pointF.x, pointF.y)
        }

        fun updateScalePivot() {
            val convertedPivot = transformations.convertPointToTransform(originScalePivot)
            scalePivot.apply {
                this.x = convertedPivot.x
                this.y = convertedPivot.y
            }
        }

        fun updateScalePivot(x: Float, y: Float) {
            originScalePivot.apply {
                this.x = x
                this.y = y
            }
            updateScalePivot()
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            Log.d(EditorViewBase::class.simpleName, "heightFocus: ${detector.focusY} just need focus: ${height/2f}")
            transformations.updateScalePivot(detector.focusX, detector.focusY + 97)
            transformations.addScale(detector.scaleFactor)
            return true
        }
    }

    private inner class ScrollListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            selectorManager.onLongPress(PointF(e.x, e.y))
            super.onLongPress(e)
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            selectorManager.onSingleTap(
                transformations.convertPointToTransform(e.x, e.y)
            )
            invalidate()
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            animateFling(PointF(-velocityX * SCROLL_VELOCITY_FACTOR, -velocityY * SCROLL_VELOCITY_FACTOR)) { dXY ->
                transformations.addTranslation(dXY.x, dXY.y)
            }
            return true
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

        private fun animateFling(velocity: PointF, update: (PointF) -> Unit) {
            ValueAnimator.ofObject(PointFEvaluator(), velocity, PointF()).apply {
                interpolator = AccelerateInterpolator()
                duration = FLING_DURATION
                addUpdateListener { animator ->
                    update(animator.animatedValue as PointF)
                    Log.d("EditorViewBase", "new value: ${animator.animatedValue}")
                }
            }.start()
        }
    }
}