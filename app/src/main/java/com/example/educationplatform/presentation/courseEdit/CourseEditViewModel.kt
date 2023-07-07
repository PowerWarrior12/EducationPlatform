package com.example.educationplatform.presentation.courseEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.Course
import com.example.educationplatform.domain.entities.Module
import com.example.educationplatform.domain.entities.Stage
import com.example.educationplatform.domain.interactors.*
import com.example.educationplatform.presentation.courseEdit.structure.adapter.StructureBlockItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CourseEditViewModel @Inject constructor(
    private val loadCourseInteractor: LoadCourseInteractor,
    private val updateCourseInteractor: UpdateCourseInteractor,
    private val loadCourseModulesInteractor: LoadCourseModulesInteractor,
    private val deleteStageInteractor: DeleteStageInteractor,
    private val deleteModuleInteractor: DeleteModuleInteractor,
    private val deleteCourseInteractor: DeleteCourseInteractor,
    private val loadDirectionsInteractor: LoadDirectionsInteractor,
    private val courseId: Int
) : ViewModel() {

    private var title: String = ""
    private var info: String = ""
    private var direction: String = ""

    private val _courseFlow = MutableSharedFlow<Course>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _structureBlockItemsFlow = MutableStateFlow(emptyList<StructureBlockItem>())
    private val _errorFlow = MutableStateFlow(false)
    private val _loadFlow = MutableStateFlow(false)
    private val _onDeleteFlow = MutableSharedFlow<Int>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _directionsFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val _courseUpdatedFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)

    val structureBlockItemsFlow = _structureBlockItemsFlow.asStateFlow()
    val courseFlow = _courseFlow.asSharedFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val loadFlow = _loadFlow.asStateFlow()
    val onDeleteFlow = _onDeleteFlow.asSharedFlow()
    val directionsFlow = _directionsFlow.asStateFlow()
    val courseUpdatedFlow = _courseUpdatedFlow.asSharedFlow()

    init {
        loadCourse()
        loadCourseModules()
        loadDirections()
    }

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateInfo(newInfo: String) {
        info = newInfo
    }

    fun updateDirection(newDirection: String) {
        direction = newDirection
    }

    fun addCourseModuleItem(module: Module) {
        viewModelScope.launch {
            val modules = _structureBlockItemsFlow.value.toMutableList()
            modules.add(modules.count() - 1, StructureBlockItem.ModuleItem(module))
            modules.add(modules.count() - 1, StructureBlockItem.AddStageItem(module.id))
            _structureBlockItemsFlow.emit(modules)
        }
    }

    fun updateCourseModuleItem(module: Module) {
        viewModelScope.launch {
            val modules = _structureBlockItemsFlow.value.toMutableList()
            val index = modules.indexOf(modules.first { block ->
                block is StructureBlockItem.ModuleItem && block.module.id == module.id
            })
            modules.removeAt(index)
            modules.add(index, StructureBlockItem.ModuleItem(module))
            _structureBlockItemsFlow.emit(modules)
        }
    }

    fun addModuleStageItem(stage: Stage) {
        viewModelScope.launch {
            val modules = _structureBlockItemsFlow.value.toMutableList()
            val index = modules.indexOf(modules.firstOrNull() {
                (it is StructureBlockItem.AddStageItem && it.moduleId == stage.moduleId)
            })
            modules.add(index, StructureBlockItem.StageItem(stage))
            _structureBlockItemsFlow.emit(modules)
        }
    }

    fun updateModuleStageItems(stage: Stage) {
        viewModelScope.launch {
            val modules = _structureBlockItemsFlow.value.toMutableList()
            val index = modules.indexOf(modules.firstOrNull {
                (it is StructureBlockItem.StageItem && it.stage.id == stage.id)
            })
            modules.removeAt(index)
            modules.add(index, StructureBlockItem.StageItem(stage))
            _structureBlockItemsFlow.emit(modules)
        }
    }

    fun deleteModule(moduleId: Int) {
        viewModelScope.launch {
            _loadFlow.emit(true)
            deleteModuleInteractor.invoke(moduleId).onSuccess {
                val modules = _structureBlockItemsFlow.value.toMutableList()
                modules.removeAll {
                    (it is StructureBlockItem.ModuleItem && it.module.id == moduleId)
                            || (it is StructureBlockItem.StageItem && it.stage.moduleId == moduleId)
                            || (it is StructureBlockItem.AddStageItem && it.moduleId == moduleId)
                }
                _structureBlockItemsFlow.emit(modules)
            }.onFailure {
                _errorFlow.emit(true)
            }
            _loadFlow.emit(false)
        }
    }

    fun deleteStage(stageId: Int) {
        viewModelScope.launch {
            _loadFlow.emit(true)
            deleteStageInteractor.invoke(stageId).onSuccess {
                val modules = _structureBlockItemsFlow.value.toMutableList()
                val index = modules.indexOf(modules.firstOrNull {
                    (it is StructureBlockItem.StageItem && it.stage.id == stageId)
                })
                modules.removeAt(index)
                _structureBlockItemsFlow.emit(modules)
            }.onFailure {
                _errorFlow.emit(true)
            }
            _loadFlow.emit(false)
        }
    }

    fun getStageById(stageId: Int): Stage {
        return (_structureBlockItemsFlow.value.first { item ->
            item is StructureBlockItem.StageItem && item.stage.id == stageId
        } as StructureBlockItem.StageItem).stage
    }

    fun getModuleById(moduleId: Int): Module {
        return (_structureBlockItemsFlow.value.first { item ->
            item is StructureBlockItem.ModuleItem && item.module.id == moduleId
        } as StructureBlockItem.ModuleItem).module
    }

    fun deleteCourse() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _loadFlow.emit(true)
                deleteCourseInteractor(courseId).onSuccess {
                    _onDeleteFlow.emit(courseId)
                }.onFailure {
                    _errorFlow.emit(true)
                }
                _loadFlow.emit(false)
            }
        }
    }

    fun updateCourse() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _loadFlow.emit(true)
                _courseFlow
                    .onEach { course ->
                        val newCourse = course.copy(title = title, info = info, direction = direction)
                        updateCourseInteractor(newCourse)
                            .onSuccess {
                                _courseUpdatedFlow.emit(Unit)
                                _courseFlow.emit(newCourse)
                            }.onFailure {
                                _errorFlow.emit(true)
                            }
                        _loadFlow.emit(false)
                        this.cancel()
                    }.collect()
            }
        }
    }

    private suspend fun prepareModulesForDisplay(modules: List<Module>) {
        val structureBlockItemList = mutableListOf<StructureBlockItem>()
        for (module in modules) {
            structureBlockItemList.add(StructureBlockItem.ModuleItem(module))
            for (stage in module.stages) {
                structureBlockItemList.add(StructureBlockItem.StageItem(stage))
            }
            structureBlockItemList.add(StructureBlockItem.AddStageItem(module.id))
        }
        structureBlockItemList.add(StructureBlockItem.AddModuleItem(courseId))
        _structureBlockItemsFlow.emit(structureBlockItemList)
    }

    private fun loadCourseModules() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadCourseModulesInteractor(courseId).onSuccess { modules ->
                    prepareModulesForDisplay(modules)
                }
            }
        }
    }

    private fun loadCourse() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadCourseInteractor(courseId).onSuccess { course ->
                    _courseFlow.emit(course)
                }.onFailure {
                    _errorFlow.emit(true)
                }
            }
        }
    }

    private fun loadDirections() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadDirectionsInteractor().onSuccess { directions ->
                    _directionsFlow.emit(directions)
                }.onFailure {
                    _errorFlow.emit(true)
                }
            }
        }
    }

    class CourseEditViewModelFactory @AssistedInject constructor(
        private val loadCourseInteractor: LoadCourseInteractor,
        private val updateCourseInteractor: UpdateCourseInteractor,
        private val loadCourseModulesInteractor: LoadCourseModulesInteractor,
        private val deleteStageInteractor: DeleteStageInteractor,
        private val deleteModuleInteractor: DeleteModuleInteractor,
        private val deleteCourseInteractor: DeleteCourseInteractor,
        private val loadDirectionsInteractor: LoadDirectionsInteractor,
        @Assisted private val courseId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CourseEditViewModel(
                loadCourseInteractor,
                updateCourseInteractor,
                loadCourseModulesInteractor,
                deleteStageInteractor,
                deleteModuleInteractor,
                deleteCourseInteractor,
                loadDirectionsInteractor,
                courseId
            ) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(@Assisted courseId: Int): CourseEditViewModelFactory
        }
    }
}