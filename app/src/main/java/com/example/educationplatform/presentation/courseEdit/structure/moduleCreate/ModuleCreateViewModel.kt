package com.example.educationplatform.presentation.courseEdit.structure.moduleCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.Module
import com.example.educationplatform.domain.interactors.CreateModuleInteractor
import com.example.educationplatform.domain.interactors.UpdateModuleInteractor
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

class ModuleCreateViewModel(
    private val createModuleInteractor: CreateModuleInteractor,
    private val updateModuleInteractor: UpdateModuleInteractor,
    private val courseId: Int,
    private val moduleId: Int
): ViewModel() {

    private val _createModuleFlow = MutableSharedFlow<Module>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _updateModuleFlow = MutableSharedFlow<Module>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableStateFlow(false)
    private val _processFlow = MutableStateFlow(false)

    val createModuleFlow = _createModuleFlow.asSharedFlow()
    val updateModuleFlow = _updateModuleFlow.asSharedFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val processFlow = _processFlow.asStateFlow()

    private var title = ""
    private var info = ""
    private var score = 0

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateInfo(newInfo: String) {
        info = newInfo
    }

    fun updateScore(newScore: Int) {
        score = newScore
    }

    fun save() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                val module = Module(moduleId, title, info, courseId, score, emptyList())
                if (moduleId < 0) {
                    createModuleInteractor(module).onSuccess { newModuleId ->
                        _createModuleFlow.emit(module.copy(id = newModuleId))
                    }.onFailure {
                        _errorFlow.emit(true)
                    }
                } else {
                    updateModuleInteractor(module).onSuccess {
                        _updateModuleFlow.emit(module)
                    }.onFailure {
                        _errorFlow.emit(true)
                    }
                }
                _processFlow.emit(false)
            }
        }
    }

    class ModuleCreateViewModelFactory @AssistedInject constructor(
        private val createModuleInteractor: CreateModuleInteractor,
        private val updateModuleInteractor: UpdateModuleInteractor,
        @Assisted("courseId")private val courseId: Int,
        @Assisted("moduleId")private val moduleId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ModuleCreateViewModel (
                createModuleInteractor,
                updateModuleInteractor,
                courseId,
                moduleId
            ) as T
        }
        @AssistedFactory
        interface Factory {
            fun create(@Assisted("courseId")courseId: Int, @Assisted("moduleId")moduleId: Int): ModuleCreateViewModelFactory
        }
    }

}