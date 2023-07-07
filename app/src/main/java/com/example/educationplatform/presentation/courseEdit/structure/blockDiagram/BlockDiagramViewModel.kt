package com.example.educationplatform.presentation.courseEdit.structure.blockDiagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlockDiagramViewModel: ViewModel() {
    private val _dataFlow = MutableStateFlow<String>("")

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