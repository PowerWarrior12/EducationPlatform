package com.example.educationplatform.presentation.createCourse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.Course
import com.example.educationplatform.domain.interactors.CourseCreateInteractor
import com.example.educationplatform.domain.interactors.LoadDirectionsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CourseCreateViewModel @Inject constructor(
    private val courseCreateInteractor: CourseCreateInteractor,
    private val loadDirectionsInteractor: LoadDirectionsInteractor
) : ViewModel() {

    private val _errorFlow = MutableStateFlow(false)
    private val _processFlow = MutableStateFlow(false)
    private val _courseCreatedFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _directionsFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    private var title: String = ""
    private var info: String = ""
    private var direction: String = ""

    val errorFlow = _errorFlow.asStateFlow()
    val processFlow = _processFlow.asStateFlow()
    val courseCreatedFlow = _courseCreatedFlow.asStateFlow()
    val directionsFlow = _directionsFlow.asStateFlow()

    init {
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

    fun createCourse() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                val creatingResult = courseCreateInteractor(
                    Course(
                        title = title,
                        info = info,
                        direction = direction,
                        creatorId = -1
                    )
                )
                _processFlow.emit(false)
                creatingResult.onSuccess { courseId ->
                    _courseCreatedFlow.emit(courseId)
                }.onFailure {
                    _errorFlow.emit(true)
                }
                this.cancel()


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

    class CourseCreateViewModelFactory @Inject constructor(
        private val courseCreateInteractor: CourseCreateInteractor,
        private val loadDirectionsInteractor: LoadDirectionsInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CourseCreateViewModel(
                courseCreateInteractor, loadDirectionsInteractor
            ) as T
        }
    }
}