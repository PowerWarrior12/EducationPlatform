package com.example.educationplatform.presentation.cource.module.blockDiagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.CheckResult
import com.example.educationplatform.domain.entities.TakesStage
import com.example.educationplatform.domain.interactors.UpdateTakesStageInteractor
import com.example.educationplatform.presentation.cource.module.ModuleAnswer
import com.example.educationplatform.presentation.entities.BlockStageData
import com.example.educationplatform.presentation.entities.TestData
import com.example.educationtools.base.EditorViewBase
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BlockDiagramDisplayViewModel constructor(
    private val updateTakesStageInteractor: UpdateTakesStageInteractor
) : ViewModel() {
    private val moshi = Moshi.Builder().build()
    private val blockDataAdapter: JsonAdapter<BlockStageData> =
        moshi.adapter(BlockStageData::class.java)
    private val moduleAnswerAdapter: JsonAdapter<ModuleAnswer> =
        moshi.adapter(ModuleAnswer::class.java)
    private val blockDiagramInspector = BlockDiagramInspector()

    private val testDataList = mutableListOf<TestData>()
    private var takesStage: TakesStage? = null

    private val _titleFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _infoFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _blockDataFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _taskFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _testDataFlow = MutableSharedFlow<List<TestData>>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _checkFlow = MutableSharedFlow<CheckResult>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _updateStage = MutableSharedFlow<TakesStage>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _processFlow = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)

    val titleFlow = _titleFlow.asSharedFlow()
    val infoFlow = _infoFlow.asSharedFlow()
    val blockDataFlow = _blockDataFlow.asSharedFlow()
    val testDataFlow = _testDataFlow.asSharedFlow()
    val taskFlow = _taskFlow.asSharedFlow()
    val checkFlow = _checkFlow.asSharedFlow()
    val updateStage = _updateStage.asSharedFlow()
    val processFlow = _processFlow.asSharedFlow()

    fun setStage(stage: TakesStage) {
        viewModelScope.launch {

            val blockStageData = blockDataAdapter.fromJson(stage.data)
            val answer =
                if (stage.answer != "") moduleAnswerAdapter.fromJson(stage.answer) else null

            blockStageData?.let {
                _titleFlow.emit(stage.title)
                _infoFlow.emit(stage.info)
                _blockDataFlow.emit(answer?.answer ?: blockStageData.diagramData)
                _taskFlow.emit(blockStageData.task)
                _testDataFlow.emit(blockStageData.testData)
                testDataList.clear()
                testDataList.addAll(blockStageData.testData)
                _checkFlow.emit(CheckResult(answer?.message, stage.isDone))
                takesStage = stage
            }
        }
    }

    fun updateBlockData(data: String) {
        viewModelScope.launch {
            _blockDataFlow.emit(data)
        }
    }

    fun checkBlockDiagram(blockDiagramEditor: EditorViewBase) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                val dataTestList = testDataList.map { testData ->
                    Pair(testData.inputData, testData.outputData)
                }
                val result =
                    blockDiagramInspector.inspect(blockDiagramEditor, dataTestList)
                takesStage?.let { takesStage ->
                    val answer =
                        ModuleAnswer(blockDiagramEditor.saveConfigurations(), result.message!!)
                    val answerData = moduleAnswerAdapter.toJson(answer)
                    val newUserScore = if (result.status) {
                        takesStage.totalScore
                    } else {
                        takesStage.userScore
                    }
                    this@BlockDiagramDisplayViewModel.takesStage =
                        takesStage.copy(isDone = result.status, answer = answerData, userScore = newUserScore)
                    updateTakesStageInteractor(this@BlockDiagramDisplayViewModel.takesStage!!)
                    _updateStage.emit(this@BlockDiagramDisplayViewModel.takesStage!!)
                }

                _processFlow.emit(false)
                _checkFlow.emit(result)
            }
        }
    }

    class BlockDiagramDisplayViewModelFactory @Inject constructor(
        private val updateTakesStageInteractor: UpdateTakesStageInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BlockDiagramDisplayViewModel(
                updateTakesStageInteractor
            ) as T
        }
    }
}