package com.example.educationplatform.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.interactors.LoadCoursesInteractor
import com.example.educationplatform.presentation.catalog.adapter.CourseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatalogViewModel @Inject constructor(
    private val loadCoursesInteractor: LoadCoursesInteractor
) : ViewModel() {

    private val skeletonsData = List(1) {
        CourseItem.LoadingItem
    }

    private val _coursesFlow = MutableSharedFlow<List<CourseItem>>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableSharedFlow<Boolean>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _processFlow = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)

    val courseFlow = _coursesFlow.asSharedFlow()
    val errorFlow = _errorFlow.asSharedFlow()
    val processFlow = _processFlow.asSharedFlow()

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                _coursesFlow.emit(skeletonsData)
                loadCoursesInteractor.invoke().onSuccess { courses ->
                    _coursesFlow.emit(courses.map { course ->
                        CourseItem.SimpleCourseItem(course)
                    })
                }.onFailure {
                    _errorFlow.emit(true)
                    _coursesFlow.emit(emptyList())
                }
                _processFlow.emit(false)
            }
        }
    }

    class CatalogViewModelFactory @Inject constructor(
        private val loadCoursesInteractor: LoadCoursesInteractor
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatalogViewModel(loadCoursesInteractor) as T
        }
    }
}