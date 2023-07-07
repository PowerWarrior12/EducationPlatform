package com.example.educationplatform.presentation.cource.module.coding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.CheckCodeData
import com.example.educationplatform.domain.entities.CheckResult
import com.example.educationplatform.domain.entities.TakesStage
import com.example.educationplatform.domain.interactors.CheckCodeInteractor
import com.example.educationplatform.domain.interactors.UpdateTakesStageInteractor
import com.example.educationplatform.presentation.cource.module.ModuleAnswer
import com.example.educationplatform.presentation.entities.CodeStageData
import com.example.educationplatform.presentation.entities.TestData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CodingDisplayViewModel(
    private val checkCodeInteractor: CheckCodeInteractor,
    private val updateTakesStageInteractor: UpdateTakesStageInteractor
) : ViewModel() {
    private val moshi = Moshi.Builder().build()
    private val codeDataAdapter: JsonAdapter<CodeStageData> =
        moshi.adapter(CodeStageData::class.java)
    private val moduleAnswerAdapter: JsonAdapter<ModuleAnswer> =
        moshi.adapter(ModuleAnswer::class.java)

    private var language = ""
    private val testDataList = mutableListOf<TestData>()
    private var takesStage: TakesStage? = null

    private val _titleFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _infoFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _codeDataFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _languageFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _taskFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _testDataFlow = MutableSharedFlow<List<TestData>>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _codeCheckFlow = MutableSharedFlow<CheckResult>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _updateStage = MutableSharedFlow<TakesStage>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _processFlow = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)

    val titleFlow = _titleFlow.asSharedFlow()
    val infoFlow = _infoFlow.asSharedFlow()
    val codeDataFlow = _codeDataFlow.asSharedFlow()
    val languageFlow = _languageFlow.asSharedFlow()
    val testDataFlow = _testDataFlow.asSharedFlow()
    val taskFlow = _taskFlow.asSharedFlow()
    val codeCheckFlow = _codeCheckFlow.asSharedFlow()
    val updateStage = _updateStage.asSharedFlow()
    val processFlow = _processFlow.asSharedFlow()

    fun setStage(stage: TakesStage) {
        viewModelScope.launch {

            val codeStageData = codeDataAdapter.fromJson(stage.data)
            val answer = if (stage.answer != "") moduleAnswerAdapter.fromJson(stage.answer) else null

            codeStageData?.let {
                _titleFlow.emit(stage.title)
                _infoFlow.emit(stage.info)
                _languageFlow.emit(codeStageData.language)
                language = codeStageData.language
                _codeDataFlow.emit(answer?.answer ?: codeStageData.code)
                _taskFlow.emit(codeStageData.task)
                _testDataFlow.emit(codeStageData.testData)
                testDataList.clear()
                testDataList.addAll(codeStageData.testData)
                _codeCheckFlow.emit(CheckResult(answer?.message, stage.isDone))
                takesStage = stage
            }
        }
    }

    fun checkCode(code: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                val result = checkCodeInteractor(CheckCodeData(code, language, testDataList.map { testData ->
                    Pair(testData.inputData, testData.outputData)
                }))
                takesStage?.let { takesStage ->
                    val answer = ModuleAnswer(code, result.message!!)
                    val answerData = moduleAnswerAdapter.toJson(answer)
                    val newUserScore = if (result.status) {
                        takesStage.totalScore
                    } else {
                        takesStage.userScore
                    }
                    this@CodingDisplayViewModel.takesStage = takesStage.copy(isDone = result.status, answer = answerData, userScore = newUserScore)
                    updateTakesStageInteractor(this@CodingDisplayViewModel.takesStage!!)
                    _updateStage.emit(this@CodingDisplayViewModel.takesStage!!)
                }
                _processFlow.emit(false)
                _codeCheckFlow.emit(result)
            }
        }
    }

    class CodingDisplayViewModelFactory @Inject constructor(
        private val checkCodeInteractor: CheckCodeInteractor,
        private val updateTakesStageInteractor: UpdateTakesStageInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CodingDisplayViewModel(
                checkCodeInteractor,
                updateTakesStageInteractor
            ) as T
        }
    }
}