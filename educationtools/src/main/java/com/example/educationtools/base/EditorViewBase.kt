package com.example.educationtools.base

import android.animation.PointFEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.*
import android.view.View.OnDragListener
import android.view.animation.AccelerateInterpolator
import androidx.core.graphics.withMatrix
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import com.example.educationtools.R
import com.example.educationtools.blocks.*
import com.example.educationtools.connection.ConnectionManager
import com.example.educationtools.dragging.DragAndDropManager
import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.logic.Variable
import com.example.educationtools.menu.BlockMenuItem
import com.example.educationtools.menu.BlocksAdapter
import com.example.educationtools.selection.SelectManager
import com.example.educationtools.selection.Selectable
import com.example.educationtools.touching.TouchManager
import com.example.educationtools.touching.Touchable
import com.example.educationtools.utils.NO_START_BLOCK_MESSAGE
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import com.example.educationtools.utils.Transformations
import com.example.educationtools.utils.extensions.roundToLesserValue
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.math.min
import kotlin.math.roundToInt

const val SCROLL_VELOCITY_FACTOR = 0.005f
const val FLING_DURATION = 100L
const val CELL_SIZE = 50f
const val MAX_SCALE = 4f
const val MIN_SCALE = 0.25f

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
    private val connectionManager =
        ConnectionManager(memoryModel, touchManager, this, selectManager)
    private val jsonFactory = JsonFactory()

    private val onBlockDoubleTouchListeners = mutableListOf<(EditableBlockBase) -> Unit>()
    private var onErrorListener: ((errorMessage: String) -> Unit)? = null
    private var onSuccessListener: ((variables: List<Variable>) -> Unit)? = null
    private var startBlock: StartBlockView? = null
    private var endBlock: EndBlockView? = null

    private var interaction = true
    private val demonstrationRect = RectF()

    private val onDragListener = OnDragListener { view, dragEvent ->
        when (dragEvent.action) {
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
                when (dragEvent.clipData.getItemAt(0).text) {
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

    private val gridPaint = Paint().apply {
        color = Color.argb(50, 180, 180, 180)
        isAntiAlias = true
        strokeWidth = 3f
        style = Paint.Style.FILL
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
        if (!interaction) {
            setDemonstrativeRect()
        }
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
                    pivotX = width / 2 - transformations.translation.x,
                    pivotY = height / 2 - transformations.translation.y
                ) {
                    drawGrid()
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

    fun setInteraction(interaction: Boolean) {
        this.interaction = interaction
        invalidate()
    }

    fun saveConfigurations(): String {
        return jsonFactory.saveConfigurations()
    }

    fun loadConfigurations(jsonConfiguration: String) {
        if (jsonConfiguration != "") {
            jsonFactory.loadData(jsonConfiguration)
        }
    }

    private fun setDemonstrativeRect() {

        if (width == 0 && height == 0) return

        var left = Float.MAX_VALUE
        var right = Float.MIN_VALUE
        var top = Float.MAX_VALUE
        var bottom = Float.MIN_VALUE

        children.forEach { child ->
            val childLeft = child.getCenter().x - child.getWidth() / 2
            val childRight = child.getCenter().x + child.getWidth() / 2
            val childTop = child.getCenter().y - child.getHeight() / 2
            val childBottom = child.getCenter().y + child.getHeight() / 2

            if (childLeft < left) {
                left = childLeft
            }
            if (childRight > right) {
                right = childRight
            }
            if (childTop < top) {
                top = childTop
            }
            if (childBottom > bottom) {
                bottom = childBottom
            }
        }

        if (left == Float.MAX_VALUE) {
            left = 0f
            right = width.toFloat()
            top = 0f
            bottom = height.toFloat()
        }

        demonstrationRect.apply {
            this.left = left - 60f
            this.right = right + 60f
            this.top = top - 60f
            this.bottom = bottom + 60f
        }

        val scaleX = width / demonstrationRect.width()
        val scaleY = height / demonstrationRect.height()

        val scale = min(scaleX, scaleY)

        val newStart = ((width / scale) - width) / 2
        val newTop = ((height / scale) - height) / 2

        val translationX = newStart + demonstrationRect.left
        val translationY = newTop + demonstrationRect.top

        transformations.addTranslation(translationX, translationY)
        transformations.addScale(min(scaleX, scaleY))
        updateSelectors()
    }

    private fun updateSelectors() {
        children.forEach { block ->
            if (block is Selectable) {
                block.getSelector().update()
            }
        }
    }

    private fun Canvas.drawGrid() {
        val startX =
            (transformations.translation.x * -1 + ((width - width / transformations.scale) / 2)).roundToLesserValue(
                CELL_SIZE
            )
        val horizontalCount = width / transformations.scale / CELL_SIZE.roundToInt() + 2

        val startY =
            (transformations.translation.y * -1 + ((height - height / transformations.scale) / 2)).roundToLesserValue(
                CELL_SIZE
            )
        val verticalCount = height / transformations.scale / CELL_SIZE.roundToInt() + 2

        for (i in 0..horizontalCount.toInt()) {
            drawLine(
                startX + CELL_SIZE * i,
                startY,
                startX + CELL_SIZE * i,
                startY + CELL_SIZE * verticalCount,
                gridPaint
            )
        }

        for (i in 0..verticalCount.toInt()) {
            drawLine(
                startX,
                startY + CELL_SIZE * i,
                startX + CELL_SIZE * horizontalCount,
                startY + CELL_SIZE * i,
                gridPaint
            )
        }
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
        if (child is Touchable) {
            touchManager.addTouchable(child)
        }
        if (child is LogicBlockView) {
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

    override fun deleteBlock(block: EditableBlock) {
        children.remove(block)
        if (block is Touchable) {
            touchManager.deleteTouchable(block)
        }
        if (block is LogicBlockView) {
            connectionManager.deleteLogicBlockView(block)
            if (block is StartBlockView) {
                startBlock = null
            }
            if (block is EndBlockView) {
                endBlock = null
            }
        }
        invalidate()
    }

    override fun getGridSize(): Float {
        return 50f
    }

    override fun getScale(): Float {
        return transformations.scale
    }

    fun generateMenuAdapter(): BlocksAdapter {
        val blockAdapter = BlocksAdapter()

        blockAdapter.submitList(
            listOf(
                BlockMenuItem(
                    CalculationBlockView::class.simpleName!!,
                    R.drawable.calculation_icon,
                    "CALCULATION BLOCK"
                ),
                BlockMenuItem(
                    ConditionBlockView::class.simpleName!!,
                    R.drawable.condition_icon,
                    "CONDITION BLOCK"
                ),
                BlockMenuItem(
                    StartBlockView::class.simpleName!!,
                    R.drawable.start_icon,
                    "START BLOCK"
                ),
                BlockMenuItem(
                    WhileDoBlockView::class.simpleName!!,
                    R.drawable.while_do_icon,
                    "WHILE DO BLOCK"
                ),
                BlockMenuItem(EndBlockView::class.simpleName!!, R.drawable.start_icon, "END BLOCK"),
            )
        )
        return blockAdapter
    }

    fun start(text: String) {
        if (checkErrorsInBlocks()) {
            onErrorListener?.invoke(SYNTAX_ERROR_TEXT)
        } else {
            try {
                startBlock?.start(text) ?: onErrorListener?.invoke(NO_START_BLOCK_MESSAGE)
            } catch (e: Exception) {
                onErrorListener?.invoke(e.message.toString())
            }
        }
    }

    private fun checkErrorsInBlocks(): Boolean {
        children.forEach { block ->
            if (block is LogicBlockView && block.isError) {
                return true
            }
        }
        return false
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
        if (!interaction) return false
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
            updateSelectors()
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

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            animateFling(
                PointF(
                    -velocityX * SCROLL_VELOCITY_FACTOR,
                    -velocityY * SCROLL_VELOCITY_FACTOR
                )
            ) { dXY ->
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
            .add(
                PolymorphicJsonAdapterFactory.of(
                    EditableBlockFactory::class.java, "type"
                )
                    .withSubtype(
                        CalculationBlockView.Configurations::class.java,
                        BlockType.CalculationType.name
                    )
                    .withSubtype(
                        ConditionBlockView.Configurations::class.java,
                        BlockType.ConditionType.name
                    )
                    .withSubtype(EndBlockView.Configurations::class.java, BlockType.EndType.name)
                    .withSubtype(
                        StartBlockView.Configurations::class.java,
                        BlockType.StartType.name
                    )
                    .withSubtype(
                        NotifyBlock.Configurations::class.java,
                        BlockType.NotificationType.name
                    )
                    .withSubtype(
                        WhileDoBlockView.Configurations::class.java,
                        BlockType.WhileDoType.name
                    )
            )

            .add(KotlinJsonAdapterFactory())
            .build()

        private val jsonAdapter = moshi.adapter(SaveEditorConfiguration::class.java)

        fun saveConfigurations(): String {
            val blocksConfigs = children.map {
                it.configuration
            }
            val connections = connectionManager.saveConnections()
            return toJsonText(SaveEditorConfiguration(blocksConfigs, connections))
        }

        fun loadData(json: String) {
            val blocksConfig = fromJsonText(json)
            clear()
            blocksConfig.blocks.forEach {
                addChild(it.create(this@EditorViewBase))
            }
            connectionManager.loadConnections(blocksConfig.connectionLines)
        }

        private fun toJsonText(calculationConfig: SaveEditorConfiguration): String {
            return jsonAdapter.toJson(calculationConfig)
        }

        private fun fromJsonText(json: String): SaveEditorConfiguration {
            return jsonAdapter.fromJson(json)!!
        }
    }

    private fun clear() {
        children.clear()
        startBlock = null
        endBlock = null
        touchManager.clear()
        connectionManager.clear()
        selectManager.clear()
        memoryModel.clear()
    }

    override fun getMemoryModel(): MemoryModel {
        return memoryModel
    }
}