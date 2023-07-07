package com.example.educationplatform.presentation.cource.module.lecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.TakesStage
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LectureDisplayViewModel: ViewModel() {

    private val _titleFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _infoFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _lectureDataFlow = MutableSharedFlow<String>(1, 1, BufferOverflow.DROP_OLDEST)

    val titleFlow = _titleFlow.asSharedFlow()
    val infoFlow = _infoFlow.asSharedFlow()
    val lectureFlow = _lectureDataFlow.asSharedFlow()


    init {
        viewModelScope.launch {
            titleFlow
                .onEach {
                    val x = it
                }.collect()
        }
    }

    fun setStage(stage: TakesStage) {
        viewModelScope.launch {
            _titleFlow.emit(stage.title)
            _infoFlow.emit(stage.info)
            _lectureDataFlow.emit(stage.data)
        }
    }
}