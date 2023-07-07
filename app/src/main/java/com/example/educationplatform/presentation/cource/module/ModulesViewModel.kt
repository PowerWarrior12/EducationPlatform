package com.example.educationplatform.presentation.cource.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.TakesModule
import com.example.educationplatform.domain.entities.TakesStage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModulesViewModel constructor(

) : ViewModel() {

    private val modules: MutableList<TakesModule> = mutableListOf()
    private var currentModuleId: Int? = null

    private val _currentModuleFlow =
        MutableSharedFlow<TakesModuleDisplay>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableStateFlow(false)
    private val _nextStepFlow = MutableSharedFlow<Int>(0, 1, BufferOverflow.DROP_OLDEST)

    val currentModuleFlow = _currentModuleFlow.asSharedFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val nextStepFlow = _nextStepFlow.asSharedFlow()

    fun setModules(newModules: List<TakesModule>) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                modules.clear()
                modules.addAll(newModules)
            }
        }
    }

    fun goToNextStageOrModule(stageIndex: Int) {
        viewModelScope.launch {
            _nextStepFlow.emit(stageIndex)
        }
    }

    fun getNextModuleIdOrNull(): Int? {
        val currentIndex = modules.indexOf(modules.first { it.id == currentModuleId })
        return if (currentIndex + 1 < modules.count() && modules[currentIndex].stages.fold(0) { acc, c ->
                acc + c.userScore
            } >= modules[currentIndex].stages.fold(0) { acc, c ->
                acc + c.totalScore
            }) {
            modules[currentIndex + 1].id
        } else null
    }

    fun updateCurrentModule(moduleId: Int) {
        currentModuleId = moduleId
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                modules.firstOrNull { takesModule ->
                    takesModule.id == moduleId
                }?.let { takesModule ->
                    _currentModuleFlow.emit(
                        TakesModuleDisplay(
                            id = takesModule.id,
                            title = "${modules.indexOf(takesModule) + 1}. ${takesModule.title}",
                            stages = takesModule.stages
                        )
                    )
                } ?: _errorFlow.emit(true)
            }
        }
    }

    fun updateTakesStage(takesStage: TakesStage) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val stages = modules.first { takesStage.moduleId == it.id }
                    .stages
                val index = stages.indexOfFirst { takesStage.id == it.id }
                stages.removeAt(index)
                stages.add(index, takesStage)
            }
        }
    }

    class ModulesViewModelFactory @Inject constructor(

    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ModulesViewModel() as T
        }
    }
}