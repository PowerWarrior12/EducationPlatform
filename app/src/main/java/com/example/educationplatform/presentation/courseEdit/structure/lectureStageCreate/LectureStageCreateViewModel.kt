package com.example.educationplatform.presentation.courseEdit.structure.lectureStageCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.Stage
import com.example.educationplatform.domain.interactors.CreateStageInteractor
import com.example.educationplatform.domain.interactors.UpdateStageInteractor
import com.example.educationplatform.presentation.courseEdit.structure.StageTypes
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LectureStageCreateViewModel(
    private val createStageInteractor: CreateStageInteractor,
    private val updateStageInteractor: UpdateStageInteractor,
    private val moduleId: Int,
    private val stageId: Int
) : ViewModel() {

    private val _newStageFlow = MutableSharedFlow<Stage>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _updateStageFlow = MutableSharedFlow<Stage>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableStateFlow(false)
    private val _processFlow = MutableStateFlow(false)

    val newStageFlow = _newStageFlow.asSharedFlow()
    val updateStageFlow = _updateStageFlow.asSharedFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val processFlow = _processFlow.asStateFlow()

    private var title: String = ""
    private var info: String = ""
    private var data: String = ""

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateInfo(newInfo: String) {
        info = newInfo
    }

    fun updateData(newData: String) {
        data = newData
    }

    fun saveStage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                val stage = Stage(stageId, title, info, data, StageTypes.Lecture.name, moduleId, 0)
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

    class LectureStageCreateViewModelFactory @AssistedInject constructor(
        private val createStageInteractor: CreateStageInteractor,
        private val updateStageInteractor: UpdateStageInteractor,
        @Assisted("moduleId") private val moduleId: Int,
        @Assisted("stageId") private val stageId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LectureStageCreateViewModel(
                createStageInteractor,
                updateStageInteractor,
                moduleId,
                stageId
            ) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("moduleId") moduleId: Int,
                @Assisted("stageId") stageId: Int
            ): LectureStageCreateViewModelFactory
        }
    }
}