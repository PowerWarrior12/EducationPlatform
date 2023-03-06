package com.example.educationtools.selection

import android.graphics.*
import com.example.educationtools.base.EditableBlock
import com.example.educationtools.touching.TouchManager

val TAG = SimpleSelector::class.simpleName
const val MAIN_OFFSET = 60f
const val EDIT_RECT_RAD = 15f
open class SimpleSelector(
    private val editableBlock: EditableBlock
): Selector {
    private var isSelected: Boolean = false
    private val selectListeners = mutableListOf<Runnable>()
    private var sideEdit: SideEdit? = null

    private val paint = Paint().apply {
        color = Color.GRAY
        isAntiAlias = true
        strokeWidth = 3f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(30f, 10f), 0f)
    }

    private val editBorderPaint = Paint().apply {
        color = Color.GRAY
        isAntiAlias = true
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    private val editFillPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        strokeWidth = 3f
        style = Paint.Style.FILL
    }

    private val mainRect = RectF().apply {
        left = editableBlock.getCenter().x - editableBlock.getWidth()/2 - MAIN_OFFSET
        right = editableBlock.getCenter().x + editableBlock.getWidth()/2 + MAIN_OFFSET
        top = editableBlock.getCenter().y - editableBlock.getHeight()/2 - MAIN_OFFSET
        bottom = editableBlock.getCenter().y + editableBlock.getHeight()/2 + MAIN_OFFSET
    }

    private val leftEditRect = RectF().apply {
        left = mainRect.left - EDIT_RECT_RAD
        right = mainRect.left + EDIT_RECT_RAD
        top = mainRect.centerY() + EDIT_RECT_RAD
        bottom = mainRect.centerY() - EDIT_RECT_RAD
    }

    private val rightEditRect = RectF().apply {
        left = mainRect.right - EDIT_RECT_RAD
        right = mainRect.right + EDIT_RECT_RAD
        top = mainRect.centerY() + EDIT_RECT_RAD
        bottom = mainRect.centerY() - EDIT_RECT_RAD
    }

    private val topEditRect = RectF().apply {
        left = mainRect.centerX() - EDIT_RECT_RAD
        right = mainRect.centerX() + EDIT_RECT_RAD
        top = mainRect.top + EDIT_RECT_RAD
        bottom = mainRect.top - EDIT_RECT_RAD
    }

    private val bottomEditRect = RectF().apply {
        left = mainRect.centerX() - EDIT_RECT_RAD
        right = mainRect.centerX() + EDIT_RECT_RAD
        top = mainRect.bottom + EDIT_RECT_RAD
        bottom = mainRect.bottom - EDIT_RECT_RAD
    }

    override fun isSelected(): Boolean {
        return isSelected
    }

    override fun select(): Boolean {
        if (isSelected) return false
        isSelected = true
        selectListeners.forEach { listener ->
            listener.run()
        }
        editableBlock.invalidate()
        return true
    }

    override fun deselect(): Boolean {
        if (!isSelected) return false
        isSelected = false
        editableBlock.invalidate()
        return true
    }

    override fun addOnSelectListener(runnable: Runnable) {
        selectListeners.add(runnable)
    }

    override fun drawSelection(canvas: Canvas) {
        if (isSelected) {
            canvas.apply {
                drawRect(mainRect, paint)
                //Fill
                drawRect(leftEditRect, editFillPaint)
                drawRect(rightEditRect, editFillPaint)
                drawRect(topEditRect, editFillPaint)
                drawRect(bottomEditRect, editFillPaint)
                //Border
                drawRect(leftEditRect, editBorderPaint)
                drawRect(rightEditRect, editBorderPaint)
                drawRect(topEditRect, editBorderPaint)
                drawRect(bottomEditRect, editBorderPaint)
            }
        }
    }

    override fun move(touchInfo: TouchManager.TouchInfo) {
        if (sideEdit != null) {
            when(sideEdit) {
                SideEdit.LEFT -> {
                    val currentLeft = editableBlock.getCenter().x - editableBlock.getWidth()/2f
                    val newWidth = touchInfo.xPos - MAIN_OFFSET - currentLeft
                    val newCenterX = currentLeft + newWidth/2f
                    editableBlock.updatePosition(PointF(newCenterX, editableBlock.getCenter().y))
                    editableBlock.updateSize(newWidth, editableBlock.getHeight())
                }
            }
        }
    }

    override fun checkPointAvailability(x: Float, y: Float): Boolean {
        if (rightEditRect.contains(x, y)) {
            sideEdit = SideEdit.RIGHT
            return true
        }
        if (leftEditRect.contains(x, y)) {
            sideEdit = SideEdit.LEFT
            return true
        }
        if (topEditRect.contains(x, y)) {
            sideEdit = SideEdit.TOP
            return true
        }
        if (bottomEditRect.contains(x, y)) {
            sideEdit = SideEdit.BOTTOM
            return true
        }
        return false
    }

    override fun checkPointAvailability(point: PointF): Boolean {
        return checkPointAvailability(point.x, point.y)
    }

    fun updateSize() {
        mainRect.apply {
            left = editableBlock.getCenter().x - editableBlock.getWidth()/2 - MAIN_OFFSET
            right = editableBlock.getCenter().x + editableBlock.getWidth()/2 + MAIN_OFFSET
            top = editableBlock.getCenter().y - editableBlock.getHeight()/2 - MAIN_OFFSET
            bottom = editableBlock.getCenter().y + editableBlock.getHeight()/2 + MAIN_OFFSET
        }

        leftEditRect.apply {
            left = mainRect.left - EDIT_RECT_RAD
            right = mainRect.left + EDIT_RECT_RAD
            top = mainRect.centerY() + EDIT_RECT_RAD
            bottom = mainRect.centerY() - EDIT_RECT_RAD
        }

        rightEditRect.apply {
            left = mainRect.right - EDIT_RECT_RAD
            right = mainRect.right + EDIT_RECT_RAD
            top = mainRect.centerY() + EDIT_RECT_RAD
            bottom = mainRect.centerY() - EDIT_RECT_RAD
        }

        topEditRect.apply {
            left = mainRect.centerX() - EDIT_RECT_RAD
            right = mainRect.centerX() + EDIT_RECT_RAD
            top = mainRect.top + EDIT_RECT_RAD
            bottom = mainRect.top - EDIT_RECT_RAD
        }

        bottomEditRect.apply {
            left = mainRect.centerX() - EDIT_RECT_RAD
            right = mainRect.centerX() + EDIT_RECT_RAD
            top = mainRect.bottom + EDIT_RECT_RAD
            bottom = mainRect.bottom - EDIT_RECT_RAD
        }
    }

    override fun checkAndTouch(x: Float, y: Float) {

    }

    override fun getPriority(): Int {
        return 1
    }

    enum class SideEdit {
        LEFT, RIGHT, TOP, BOTTOM
    }
}