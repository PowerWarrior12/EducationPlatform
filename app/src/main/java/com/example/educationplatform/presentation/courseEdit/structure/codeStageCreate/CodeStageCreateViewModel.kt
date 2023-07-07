package com.example.educationplatform.presentation.courseEdit.structure.codeStageCreate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.Stage
import com.example.educationplatform.domain.interactors.CreateStageInteractor
import com.example.educationplatform.domain.interactors.UpdateStageInteractor
import com.example.educationplatform.presentation.courseEdit.structure.StageTypes
import com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter.TestDataItem
import com.example.educationplatform.presentation.entities.CodeStageData
import com.example.educationplatform.presentation.entities.TestData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodeStageCreateViewModel(
    private val createStageInteractor: CreateStageInteractor,
    private val updateStageInteractor: UpdateStageInteractor,
    private val moduleId: Int,
    private val stageId: Int
) : ViewModel() {
    private val moshi = Moshi.Builder().build()
    private val codeDataAdapter: JsonAdapter<CodeStageData> =
        moshi.adapter(CodeStageData::class.java)

    private val _newStageFlow = MutableSharedFlow<Stage>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _updateStageFlow = MutableSharedFlow<Stage>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableStateFlow(false)
    private val _processFlow = MutableStateFlow(false)
    private val _codeFlow = MutableStateFlow<String>("")
    private val _languageFlow = MutableStateFlow<String>("")
    private val _taskFlow = MutableStateFlow<String>("")
    private val _scoreFlow = MutableSharedFlow<Int>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _testFlow = MutableStateFlow<List<TestDataItem>>(
        listOf(
            TestDataItem.AddTestDataItem()
        )
    )

    val newStageFlow = _newStageFlow.asSharedFlow()
    val updateStageFlow = _updateStageFlow.asSharedFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val processFlow = _processFlow.asStateFlow()
    val codeFlow = _codeFlow.asStateFlow()
    val languageFlow = _languageFlow.asStateFlow()
    val taskFlow = _taskFlow.asStateFlow()
    val testFlow = _testFlow.asStateFlow()
    val scoreFlow = _scoreFlow.asSharedFlow()

    private var title: String = ""
    private var info: String = ""
    private var score: String = ""

    fun setData(data: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val codeStageData = codeDataAdapter.fromJson(data)
                    if (codeStageData != null) {
                        _codeFlow.emit(codeStageData.code)
                        _taskFlow.emit(codeStageData.task)
                        _languageFlow.emit(codeStageData.language)
                        val testItems = codeStageData.testData.mapIndexed { index, data ->
                            TestDataItem.TestDataBaseItem(data, index + 1)
                        }.toMutableList<TestDataItem>()
                        testItems.add(TestDataItem.AddTestDataItem())
                        _testFlow.emit(testItems)
                    }
                } catch (e: java.lang.Exception) {
                    _errorFlow.emit(true)
                }
            }
        }
    }

    fun addTestItem() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val testItems = _testFlow.value.toMutableList()
                testItems.add(testItems.count() - 1, TestDataItem.TestDataBaseItem(TestData("", ""), testItems.count()))
                _testFlow.emit(testItems)
            }
        }
    }

    fun deleteTestItem(number: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val testItems = _testFlow.value.toMutableList()
                testItems.removeAt(number - 1)
                val newTestItems = testItems.mapIndexed { index, testDataItem ->
                    if (testDataItem is TestDataItem.TestDataBaseItem) {
                        testDataItem.number = index + 1
                        testDataItem
                    } else {
                        testDataItem
                    }
                }
                _testFlow.emit(newTestItems)
            }
        }
    }

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateInfo(newInfo: String) {
        info = newInfo
    }

    fun updateScore(newScore: String) {
        score = newScore
    }

    fun saveCodeStageData(code: String, language: String, testData: List<TestData>, task: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                val data = codeDataAdapter.toJson(
                    CodeStageData(
                        testData = testData,
                        language = language,
                        code = code,
                        task = task,
                    )
                )
                Log.d("MyTag", data)
                val stage = Stage(
                    stageId,
                    title,
                    info,
                    data,
                    StageTypes.Code.name,
                    moduleId,
                    score.toIntOrNull() ?: 0
                )
                if (stageId < 0) {
                    createStageInteractor(stage).onSuccess { newId ->
                        _newStageFlow.emit(stage.copy(id = newId))
                    }.onFailure {
                        _errorFlow.emit(true)
                    }
                } else {
                    updateStageInteractor(stage).onSuccess {
                        _updateStageFlow.emit(stage)
                    }.onFailure {
                        _errorFlow.emit(true)
                    }
                }
                _processFlow.emit(false)
            }
        }
    }

    class CodeStageCreateViewModelFactory @AssistedInject constructor(
        private val createStageInteractor: CreateStageInteractor,
        private val updateStageInteractor: UpdateStageInteractor,
        @Assisted("moduleId") private val moduleId: Int,
        @Assisted("stageId") private val stageId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CodeStageCreateViewModel(
                createStageInteractor, updateStageInteractor, moduleId, stageId
            ) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("moduleId") moduleId: Int,
                @Assisted("stageId") stageId: Int
            ): CodeStageCreateViewModelFactory
        }
    }
}