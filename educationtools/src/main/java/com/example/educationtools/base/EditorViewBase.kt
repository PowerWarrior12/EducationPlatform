package com.example.educationtools.base

import android.animation.PointFEvaluator
import android.animation.ValueAnimator
import android.content.ClipDescription
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.view.animation.AccelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.educationtools.R
import com.example.educationtools.blocks.*
import com.example.educationtools.connection.ConnectionManager
import com.example.educationtools.dragging.DragAndDropManager
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.logic.Variable
import com.example.educationtools.logic.WhileDoBlock
import com.example.educationtools.menu.BlockMenuItem
import com.example.educationtools.menu.BlocksAdapter
import com.example.educationtools.selection.SelectManager
import com.example.educationtools.touching.TouchManager
import com.example.educationtools.touching.Touchable
import com.example.educationtools.utils.Transformations
import com.squareup.moshi.Moshi

const val SCROLL_VELOCITY_FACTOR = 0.005f
const val FLING_DURATION = 100L

class EditorViewBase @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ParentEditor {

    //Данные о изменениях экрана
    private val transformations = Transformations(0f, 0f)
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val gestureDetector = GestureDetector(context, ScrollListener())

    //Дочерние блоки
    private val children: MutableList<EditableBlock> = mutableListOf()

    private val memoryModel = MemoryModel()

    //Managers
    private val touchManager = TouchManager(transformations)
    private val selectManager = SelectManager(touchManager)
    private val dragAndDropManager = DragAndDropManager(touchManager, transformations)
    private val connectionManager = ConnectionManager(memoryModel, touchManager, this, selectManager)
    private val jsonFactory = JsonFactory()

    private val onBlockDoubleTouchListeners = mutableListOf<(EditableBlockBase) -> Unit>()
    private var onErrorListener: ((errorMessage: String) -> Unit)? = null
    private var onSuccessListener: ((variables: List<Variable>) -> Unit)? = null
    private var startBlock: StartBlockView? = null
    private var endBlock: EndBlockView? = null

    private val onDragListener = OnDragListener { view, dragEvent ->
        when(dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                true
            }
            DragEvent.ACTION_DROP -> {
                val position = transformations.convertPointToTransform(dragEvent.x, dragEvent.y)
                when(dragEvent.clipData.getItemAt(0).text) {
                    CalculationBlockView::class.simpleName -> {
                        addChild(CalculationBlockView().apply {
                            setEditorParent(this@EditorViewBase)
                            updatePosition(position)
                            updateSize(400f, 250f)
                        })
                    }
                    ConditionBlockView::class.simpleName -> {
                        addChild(ConditionBlockView().apply {
                            setEditorParent(this@EditorViewBase)
                            updatePosition(position)
                            updateSize(400f, 250f)
                        })
                    }
                    StartBlockView::class.simpleName -> {
                        addChild(StartBlockView().apply {
                            setEditorParent(this@EditorViewBase)
                            updatePosition(position)
                            updateSize(400f, 250f)
                        })
                    }
                    WhileDoBlockView::class.simpleName -> {
                        addChild(WhileDoBlockView().apply {
                            setEditorParent(this@EditorViewBase)
                            updatePosition(position)
                            updateSize(400f, 250f)
                        })
                    }
                    EndBlockView::class.simpleName -> {
                        addChild(EndBlockView().apply {
                            setEditorParent(this@EditorViewBase)
                            updatePosition(position)
                            updateSize(400f, 250f)
                        })
                    }
                }
                true
            }
            else -> {
                false
            }
        }

    }
    init {
        selectManager.start()
        dragAndDropManager.start()
        touchManager.addDoubleTouchListener(::onDoubleTouch)

        this.setOnDragListener(onDragListener)
        connectionManager.addOnConnectedListener { start, end ->
            updateLogicBlocks(start)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        transformations.updateSize(width.toFloat(), height.toFloat())
        super.onSizeChanged(w, h, oldw, oldh)
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
                    connectionManager.draw(this)
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

    fun saveConfigurations(): String {
        return jsonFactory.saveConfigurations()
    }

    private fun onDoubleTouch(touchInfo: TouchManager.TouchInfo) {
        if (touchInfo is TouchManager.TouchInfo.FilledInfo && touchInfo.touchable is EditableBlockBase) {
            onBlockDoubleTouchListeners.forEach {
                it(touchInfo.touchable)
            }
        }
    }

    private fun updateLogicBlocks(startId: String) {
        val dependenciesBlocks = memoryModel.getDependentBlocks(startId)
        for (id in dependenciesBlocks) {
            val block = children.firstOrNull {
                if (it is LogicBlockView && it.logicBlock.id == id) {
                    return@firstOrNull true
                }
                false
            }
            if (block != null) {
                (block as LogicBlockView).checkError()
                continue
            }
        }
    }

    fun addOnBlockDoubleTouchListener(listener: (block: EditableBlockBase) -> Unit) {
        onBlockDoubleTouchListeners.add(listener)
    }

    fun addChild(child: EditableBlock) {
        children.add(child)
        child.setEditorParent(this)
        if (child is Touchable) {
            touchManager.addTouchable(child)
        }
        if (child is LogicBlockView) {
            child.setMemoryModel(memoryModel)
            child.setTextChangeListener(::updateLogicBlocks)
            connectionManager.addLogicBlockView(child)
            if (child is StartBlockView) {
                startBlock = child
            }
            if (child is EndBlockView) {
                endBlock = child
                onSuccessListener?.let {
                    endBlock!!.addOnSuccessListener(it)
                }
            }
        }
        invalidate()
    }

    fun generateMenuAdapter(): BlocksAdapter {
        val blockAdapter = BlocksAdapter()

        blockAdapter.submitList(listOf(
            BlockMenuItem(CalculationBlockView::class.simpleName!!, R.drawable.calculation_icon, "CALCULATION BLOCK"),
            BlockMenuItem(ConditionBlockView::class.simpleName!!, R.drawable.condition_icon, "CONDITION BLOCK"),
            BlockMenuItem(StartBlockView::class.simpleName!!, R.drawable.start_icon, "START BLOCK"),
            BlockMenuItem(WhileDoBlockView::class.simpleName!!, R.drawable.while_do_icon, "WHILE DO BLOCK"),
            BlockMenuItem(EndBlockView::class.simpleName!!, R.drawable.start_icon, "END BLOCK"),
        ))
        return blockAdapter
    }

    fun start(text: String): Boolean {
        try {
            startBlock?.start(text) ?: return false
        } catch (e: Exception) {
            onErrorListener?.invoke(e.message.toString())
        }
        return true
    }

    fun addOnErrorListener(listener: (errorMessage: String) -> Unit) {
        onErrorListener = listener
    }

    fun addOnSuccessListener(listener: (variables: List<Variable>) -> Unit) {
        onSuccessListener = listener
        endBlock?.addOnSuccessListener(listener)
    }

    /**
     * Обработка тачей
     */
    private fun processTouch(event: MotionEvent): Boolean {
        return if (event.pointerCount > 1) {
            touchManager.onTwoPointTap(event.getX(0), event.getY(0))
            scaleGestureDetector.onTouchEvent(event)
        } else {
            if (event.action == MotionEvent.ACTION_MOVE) {
                touchManager.move(event.x, event.y)
            } else if (event.action == MotionEvent.ACTION_UP) {
                touchManager.releaseTouch(event.x, event.y)
            }
            gestureDetector.onTouchEvent(event)
        }
    }



    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            transformations.addScale(detector.scaleFactor)
            invalidate()
            return true
        }
    }

    private inner class ScrollListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            touchManager.touchDown(e.x, e.y)
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            touchManager.longTouch(e.x, e.y)
            super.onLongPress(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            touchManager.singleTouch(e.x, e.y)
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            touchManager.doubleTouch(e.x, e.y)
            return true
        }

        override fun onShowPress(e: MotionEvent) {
            super.onShowPress(e)
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            animateFling(PointF(-velocityX * SCROLL_VELOCITY_FACTOR, -velocityY * SCROLL_VELOCITY_FACTOR)) { dXY ->
                transformations.addTranslation(dXY.x, dXY.y)
                invalidate()
            }
            return true
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (!touchManager.isProcess()) {
                transformations.addTranslation(
                    distanceX,
                    distanceY
                )
                invalidate()
                return true
            }
            return false
        }

        private fun animateFling(velocity: PointF, update: (PointF) -> Unit) {
            ValueAnimator.ofObject(PointFEvaluator(), velocity, PointF()).apply {
                interpolator = AccelerateInterpolator()
                duration = FLING_DURATION
                addUpdateListener { animator ->
                    update(animator.animatedValue as PointF)
                }
            }.start()
        }
    }

    inner class JsonFactory {

        private val moshi = Moshi.Builder()
            .add(EditableBlockFactory::class.java, conver)
            .build()

        private val jsonAdapter = moshi.adapter(SaveEditorConfiguration::class.java)

        fun saveConfigurations(): String {
            val blocksConfigs = children.map {
                it.configuration
            }
            return toJsonText(SaveEditorConfiguration(blocksConfigs))
        }

        private fun toJsonText(calculationConfig: SaveEditorConfiguration): String {
            return jsonAdapter.toJson(calculationConfig)
        }

        fun fromJsonText(json: String): SaveEditorConfiguration {
            return jsonAdapter.fromJson(json)!!
        }
    }
}