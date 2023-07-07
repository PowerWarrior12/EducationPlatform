package com.example.educationplatform.domain.entities

import java.util.*

data class Report(
    val id: Int,
    val userName: String,
    val userIcon: String,
    val text: String,
    val rating: Int,
    val courseId: Int,
    val date: Date
)