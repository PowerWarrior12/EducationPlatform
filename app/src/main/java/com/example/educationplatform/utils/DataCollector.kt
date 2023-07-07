package com.example.educationplatform.utils

interface DataCollector<T: Any?> {
    fun addDataSource(dataSource: () -> T)
    fun deleteDataSource(dataSource: () -> T)
}