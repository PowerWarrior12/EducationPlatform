package com.example.educationplatform.presentation.courseEdit.structure.blockStageCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.Stage
import com.example.educationplatform.domain.interactors.CreateStageInteractor
import com.example.educationplatform.domain.interactors.UpdateStageInteractor
import com.example.educationplatform.presentation.courseEdit.structure.StageTypes
import com.example.educationplatform.presentation.entities.BlockStageData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlockStageCreateViewModel(
    private val createStageInteractor: CreateStageInteractor,
    private val updateStageInteractor: UpdateStageInteractor,
    private val moduleId: Int,
    private val stageId: Int
) : ViewModel() {
    private val moshi = Moshi.Builder().build()
    private val diagramDataAdapter: JsonAdapter<BlockStageData> =
        moshi.adapter(BlockStageData::class.java)
    private val _newStageFlow = MutableSharedFlow<Stage>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _updateStageFlow = MutableSharedFlow<Stage>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableStateFlow(false)
    private val _processFlow = MutableStateFlow(false)
    private val _dataFlow = MutableSharedFlow<BlockStageData>(1, 1, BufferOverflow.DROP_OLDEST)

    val newStageFlow = _newStageFlow.asSharedFlow()
    val updateStageFlow = _updateStageFlow.asSharedFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val processFlow = _processFlow.asStateFlow()
    val dataFlow = _dataFlow.asSharedFlow()

    private var title: String = ""
    private var info: String = ""
    private var score: String = ""

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateInfo(newInfo: String) {
        info = newInfo
    }

    fun updateScore(newScore: String) {
        score = newScore
    }

    fun setData(data: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val blockStageData = try {
                    diagramDataAdapter.fromJson(data)
                } catch (e: Exception) {
                    BlockStageData(emptyList(), "", "")
                }

                if (blockStageData != null) {
                    _dataFlow.emit(blockStageData)
                } else {
                    _errorFlow.emit(true)
                }
            }
        }
    }

    fun saveData(blockStageData: BlockStageData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                val data = diagramDataAdapter.toJson(
                    blockStageData
                )
                val stage = Stage(
                    stageId,
                    title,
                    info,
                    data,
                    StageTypes.BlockDiagram.name,
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

    class BlockStageCreateViewModelFactory @AssistedInject constructor(
        private val createStageInteractor: CreateStageInteractor,
        private val updateStageInteractor: UpdateStageInteractor,
        @Assisted("moduleId") private val moduleId: Int,
        @Assisted("stageId") private val stageId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BlockStageCreateViewModel(
                createStageInteractor, updateStageInteractor, moduleId, stageId
            ) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("moduleId") moduleId: Int,
                @Assisted("stageId") stageId: Int
            ): BlockStageCreateViewModelFactory
        }
    }
}