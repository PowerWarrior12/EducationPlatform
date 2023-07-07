package com.example.educationplatform.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.domain.interactors.GetStatusInteractor
import com.example.educationplatform.domain.interactors.LoadEditCoursesInteractor
import com.example.educationplatform.domain.interactors.LoadTakesCoursesInteractor
import com.example.educationplatform.presentation.home.mainAdapter.UsersCourseItem
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.coroutineContext

private const val TEACHER_STATUS = "TEACHER"

class HomeViewModel(
    private val loadTakesCoursesInteractor: LoadTakesCoursesInteractor,
    private val loadEditCoursesInteractor: LoadEditCoursesInteractor,
    private val getStatusInteractor: GetStatusInteractor
) : ViewModel() {
    private val _takesCoursesFlow = MutableSharedFlow<List<UsersCourseItem>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val _processFlow = MutableStateFlow(false)
    private val _errorFlow = MutableStateFlow(false)

    val takesCoursesFlow = _takesCoursesFlow.asSharedFlow()
    val processFlow = _processFlow.asStateFlow()
    val errorFlow = _errorFlow.asStateFlow()

    init {
        updateCourses()
    }

    fun updateCourses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _errorFlow.emit(false)
                _processFlow.emit(true)
                val teacherStatus = getStatusInteractor() == TEACHER_STATUS

                val takesCoursesAsync = loadTakesCoursesAsync()
                val editCoursesResult = if (teacherStatus) {
                    val editCoursesAsync = loadEditCoursesAsync()
                    editCoursesAsync.await()
                } else {
                    Result.success(emptyList())
                }

                val takesCoursesResult = takesCoursesAsync.await()


                if (takesCoursesResult.isFailure || editCoursesResult.isFailure) {
                    _errorFlow.emit(true)
                } else {
                    val coursesList = mutableListOf<UsersCourseItem>()

                    takesCoursesResult.onSuccess { takesCourses ->
                        if (takesCourses.isNotEmpty()) {
                            //Добавляем загаловок для курсов, на которые подписан пользователь
                            coursesList.add(
                                UsersCourseItem.TitleItem(
                                    MainApplication.INSTANCE.resources.getString(
                                        R.string.takes_courses_label
                                    )
                                )
                            )
                            //Добавляем курсы, на которые подписан пользователь
                            coursesList.addAll(takesCourses.map { takesCourse ->
                                UsersCourseItem.TakesCourseItem(takesCourse)
                            })
                        }
                    }


                    editCoursesResult.onSuccess { editCourses ->
                        if (editCourses.isNotEmpty()) {
                            //Добавляем заголовок для курсов, которые создал пользователь
                            coursesList.add(
                                UsersCourseItem.TitleItem(
                                    MainApplication.INSTANCE.resources.getString(
                                        R.string.edit_courses_label
                                    )
                                )
                            )
                            //Добавляем курсы, которые создал пользователь
                            coursesList.add(UsersCourseItem.HorizontalItems(editCourses))
                        }
                    }
                    if (teacherStatus) {
                        //Добавляем кнопку для создания нового курса
                        coursesList.add(UsersCourseItem.ButtonItem)
                    }
                    _takesCoursesFlow.emit(coursesList)
                }

                _processFlow.emit(false)
            }
        }
    }

    private suspend fun loadTakesCoursesAsync() = CoroutineScope(coroutineContext).async {
        loadTakesCoursesInteractor()
    }

    private suspend fun loadEditCoursesAsync() = CoroutineScope(coroutineContext).async {
        loadEditCoursesInteractor()
    }

    class HomeViewModelFactory @AssistedInject constructor(
        private val loadTakesCoursesInteractor: LoadTakesCoursesInteractor,
        private val loadEditCoursesInteractor: LoadEditCoursesInteractor,
        private val getStatusInteractor: GetStatusInteractor
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(
                loadTakesCoursesInteractor,
                loadEditCoursesInteractor,
                getStatusInteractor
            ) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(): HomeViewModelFactory
        }
    }
}