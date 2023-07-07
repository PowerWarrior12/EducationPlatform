package com.example.educationplatform.presentation.courseEdit.structure.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel: ViewModel() {
    private val _dataFlow = MutableStateFlow("")

    val dataFlow = _dataFlow.asStateFlow()

    fun setData(data: String) {
        viewModelScope.launch {
            _dataFlow.emit(data)
        }
    }

    fun getData(): String {
        return dataFlow.value
    }
}